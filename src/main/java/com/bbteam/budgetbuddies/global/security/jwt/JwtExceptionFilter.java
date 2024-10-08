package com.bbteam.budgetbuddies.global.security.jwt;

import com.bbteam.budgetbuddies.apiPayload.code.ErrorReasonDto;
import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    // JWT 검증 중 발생한 예외를 처리하는 필터 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();

            // 토큰이 없는 경우
            if(ErrorStatus._TOKEN_NOT_FOUND.getMessage().equals(message)) {
                log.info("error message: {}", message);
                setResponse(response, ErrorStatus._TOKEN_NOT_FOUND);
            }
            // 토큰 만료된 경우
            else if(message.startsWith("JWT expired at")) {
                log.info("error message: {}", message);
                setResponse(response, ErrorStatus._TOKEN_EXPIRED);
            }
            // 잘못된 형식의 토큰인 경우 (페이로드 혹은 시그니처 불일치)
            else if(message.startsWith("JWT signature does not match")) {
                log.info("error message: {}", message);
                setResponse(response, ErrorStatus._TOKEN_PAYLOAD_OR_SIGNATURE_NOT_VALID);
            }
            // 잘못된 형식의 토큰인 경우 (잘못된 헤더 정보)
            else if(message.startsWith("Malformed JWT JSON:")) {
                log.info("error message: {}", message);
                setResponse(response, ErrorStatus._TOKEN_HEADER_NOT_VALID);
            }
            // 그 외: 유효하지 않는 토큰
            else {
                log.info("error message: {}", message);
                setResponse(response, ErrorStatus._TOKEN_NOT_VALID);
            }

        }
    }

    // 에러 응답을 설정하는 메소드
    private void setResponse(HttpServletResponse response, ErrorStatus errorStatus) throws RuntimeException, IOException {
        // ErrorReasonDto를 빌더로 생성하여 에러 정보 설정
        ErrorReasonDto errorReason = ErrorReasonDto.builder()
            .message(errorStatus.getMessage()) // 에러 메시지
            .code(errorStatus.getCode()) // 에러 코드
            .isSuccess(false) // 성공 여부는 false
            .httpStatus(errorStatus.getHttpStatus()) // HTTP 상태 코드
            .build();

        // 응답 설정
        response.setStatus(errorReason.getHttpStatus().value()); // HTTP 상태 코드 설정
        response.setContentType("application/json; charset=UTF-8"); // JSON 형식 및 UTF-8 설정
        response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정

        // JSON 형식으로 에러 정보를 클라이언트에 전송
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorReason));
    }
}