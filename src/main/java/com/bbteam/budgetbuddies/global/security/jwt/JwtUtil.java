package com.bbteam.budgetbuddies.global.security.jwt;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security.refreshtoken.RefreshTokenRepository;
import com.bbteam.budgetbuddies.global.security.refreshtoken.RefreshTokenService;
import com.bbteam.budgetbuddies.global.security.utils.MyUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JwtUtil {

    private final MyUserDetailsService myUserDetailsService;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenService refreshTokenService;

    // JWT 토큰을 서명하는 데 사용되는 비밀 키
    @Value("${jwt.token.key}")
    private String SECRET_KEY;

    @Value("${jwt.token.access-token.expiration-time}")
    private Long accessTokenExpirationTime; // 엑세스 토큰 만료 기한

    @Value("${jwt.token.refresh-token.expiration-time}")
    private Long refreshTokenExpirationTime; // 리프레시 토큰 만료 기한

    // 토큰에서 사용자 이름(주체, subject)을 추출하는 메서드
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 만료 시간을 추출하는 메서드
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractPhoneNumber(String token) {
        // JWT 전화번호 클레임 추출
        return (String) extractAllClaims(token).get("phoneNumber");
    }

    public User extractUser(String token) {

        Long userId = Long.parseLong(extractAllClaims(token).getSubject());

        User user = myUserDetailsService.loadUserByUserId(userId);

        return user;
    }

    // 토큰에서 특정 클레임을 추출하는 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // 모든 클레임을 추출
        return claimsResolver.apply(claims); // 추출된 클레임에서 원하는 정보를 반환
    }

    // 토큰에서 모든 클레임을 추출하는 메서드
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes()) // 서명 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱
                .getBody(); // 클레임 반환
    }

    // 토큰이 만료되었는지 확인하는 메서드
    private void isTokenExpired(String token) throws GeneralException {
        if (extractExpiration(token).before(new Date())) {
            throw new GeneralException(ErrorStatus._TOKEN_EXPIRED);
        }
    }

    // 사용자 전화번호를 기반으로 엑세스 토큰을 생성하는 메서드
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>(); // 클레임으로 빈 맵을 사용
        claims.put("phoneNumber", user.getPhoneNumber());

        String token = createToken(claims, user.getId(), accessTokenExpirationTime);// 토큰 생성: 1시간 유효

        log.info("access token: {}", token);

        return token;
    }

    // 사용자 전화번호를 기반으로 리프레시 토큰을 생성하는 메서드
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>(); // 클레임으로 빈 맵을 사용
        claims.put("phoneNumber", user.getPhoneNumber());

        String token = createToken(claims, user.getId(), refreshTokenExpirationTime);// 토큰 생성: 1년 유효

        log.info("refresh token: {}", token);

        refreshTokenService.saveOrUpdateRefreshToken(user, token);

        return token;
    }

    // 클레임, 주체, 만료 시간을 기반으로 JWT 토큰을 생성하는 메서드
    private String createToken(Map<String, Object> claims, Long userId, Long expirationTime) {

        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setSubject(String.valueOf(userId)) // 주체 설정 (유저 ID)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 시간으로 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // 서명 알고리즘과 서명 키 설정
                .compact(); // 최종 토큰을 생성하여 반환
    }

    // 엑세스 토큰이 유효한지 검증하는 메서드 (사용자 이름과 만료 여부 확인)
    public Boolean validateAccessToken(String token, Long userId, String phoneNumber) throws GeneralException {
        final String extractedUserId = extractUserId(token); // 토큰에서 사용자 ID 추출
        final String extractedPhoneNumber = extractPhoneNumber(token); // 토큰에서 사용자 전화번호 추출

        log.info("extractedUserId: {}", extractedUserId);
        log.info("extractedPhoneNumber: {}", extractedPhoneNumber);

        isTokenExpired(token); // 만료 확인, 만료된 경우 예외 발생

        return (
            extractedUserId.equals(String.valueOf(userId))  // 사용자 ID 일치 여부 검증
            && extractedPhoneNumber.equals(phoneNumber)     // 사용자 전화번호 일치 여부 검증
        );
    }

    // 리프레시 토큰이 유효한지 검증하는 메서드 (사용자 이름과 만료 여부 확인 및 DB 값과 비교)
    public Boolean validateRefreshToken(String token, Long userId, String phoneNumber) {
        final String extractedUserId = extractUserId(token); // 토큰에서 사용자 ID 추출
        final String extractedPhoneNumber = extractPhoneNumber(token); // 토큰에서 사용자 전화번호 추출

        User user = myUserDetailsService.loadUserByUserId(userId);

        isTokenExpired(token); // 만료 확인, 만료된 경우 예외 발생

        // 리프레시 토큰 검증
        return (
            refreshTokenService.validateRefreshToken(user, token) &&
            extractedUserId.equals(String.valueOf(userId)) &&
            extractedPhoneNumber.equals(phoneNumber)
        );
    }
}


