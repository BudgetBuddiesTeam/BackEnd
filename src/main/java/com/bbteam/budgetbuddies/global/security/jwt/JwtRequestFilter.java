package com.bbteam.budgetbuddies.global.security.jwt;

import com.bbteam.budgetbuddies.apiPayload.code.ErrorReasonDto;
import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security.utils.CustomAuthenticationToken;
import com.bbteam.budgetbuddies.global.security.utils.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final MyUserDetailsService myUserDetailsService;

    private final String BEARER = "Bearer ";

    // Swagger 및 인증 관련 요청 주소 목록
    private final List<String> swaggers = List.of(
        "/swagger-ui",
        "/v3/api-docs"
    );
    private final List<String> auth = List.of(
        "/auth/get-otp",
        "/auth/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        String requestURI = request.getRequestURI(); // 요청한 URI 확인 (리프레시 토큰 요청 여부 확인)

        // Swagger 또는 인증 관련 URI일 경우 필터 통과
        if (swaggers.stream().anyMatch(requestURI::startsWith) || auth.stream().anyMatch(requestURI::startsWith)) {
            chain.doFilter(request, response); // 필터 통과
            return; // 메소드 종료
        }

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Authorization 헤더 추출
        Long userId = null;
        String phoneNumber = null;
        String token = null;

        // Bearer 토큰이 포함된 Authorization 헤더에서 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {
            token = authorizationHeader.substring(7); // "Bearer " 이후의 부분을 추출
            userId = Long.parseLong(jwtUtil.extractUserId(token)); // JWT에서 사용자 ID 추출
            phoneNumber = jwtUtil.extractPhoneNumber(token); // JWT에서 전화번호 추출
            log.info("request header's token: {}", token);
            log.info("request header's userId: {}", userId);
            log.info("request header's phoneNumber: {}", phoneNumber);
        }

        try {
            // 토큰이 없는 경우 예외 발생
            if (token == null) {
                throw new GeneralException(ErrorStatus._TOKEN_NOT_FOUND);
            }

            // 인증 로직
            if (userId != null && phoneNumber != null && request.getAttribute("authenticatedUser") == null) {
                if (isRefreshTokenApiRequest(requestURI)) {
                    authenticateToken(request, token, phoneNumber, true); // 리프레시 토큰 검증
                } else {
                    authenticateToken(request, token, phoneNumber, false); // 액세스 토큰 검증
                }
            }

            // 필터 체인을 계속해서 실행
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            // 토큰 만료 시 GeneralException 발생
            throw new GeneralException(ErrorStatus._TOKEN_EXPIRED);
        } catch (GeneralException ex) {
            // GeneralException 처리 및 클라이언트에 에러 응답 전송
            ErrorReasonDto errorReason = ex.getErrorReasonHttpStatus();
            response.setStatus(errorReason.getHttpStatus().value()); // HTTP 상태 코드 설정
            response.setContentType("application/json; charset=UTF-8"); // JSON 형식 및 UTF-8 설정
            response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorReason)); // JSON 형식으로 에러 응답 전송
        }
    }

    // 리프레시 토큰 API 요청 여부 확인
    private boolean isRefreshTokenApiRequest(String requestURI) {
        return "/auth/reissue-access-token".equals(requestURI);
    }

    /**
     * 공통 인증 로직: 토큰 검증 및 인증 생성
     *
     * @param request         HttpServletRequest 객체
     * @param token           JWT 토큰
     * @param phoneNumber     JWT에서 추출한 전화번호
     * @param isRefreshToken  리프레시 토큰 여부
     */
    private void authenticateToken(HttpServletRequest request, String token, String phoneNumber, boolean isRefreshToken) {
        // 사용자 정보 로드
        User user = this.myUserDetailsService.loadUserByPhoneNumber(phoneNumber);

        // 토큰 검증: 리프레시 토큰이면 리프레시 토큰 검증, 액세스 토큰이면 액세스 토큰 검증
        boolean isValidToken = isRefreshToken
            ? jwtUtil.validateRefreshToken(token, user.getId(), user.getPhoneNumber())  // 리프레시 토큰 검증
            : jwtUtil.validateAccessToken(token, user.getId(), user.getPhoneNumber());  // 액세스 토큰 검증

        if (isValidToken) {
            // 인증 객체 생성
            CustomAuthenticationToken authentication = new CustomAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            /**
             * <사용 예시>
             * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
             * if (authentication != null && authentication.isAuthenticated()) {
             *     User user = (User) authentication.getPrincipal();
             *     // User 객체를 통해 필요한 정보를 사용
             * }
             */
        } else {
            // 토큰이 유효하지 않을 경우 예외 발생
            throw new GeneralException(ErrorStatus._TOKEN_NOT_VALID);
        }
    }
}