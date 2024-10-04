package com.bbteam.budgetbuddies.global.security2;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {


    // JWT 토큰을 서명하는 데 사용되는 비밀 키
    @Value("${jwt.token.key}")
    private String SECRET_KEY;

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
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 사용자 전화번호 만료 시간을 기반으로 토큰을 생성하는 메서드
    public String generateToken(Long userId, String phoneNumber, long expirationTime) {
        Map<String, Object> claims = new HashMap<>(); // 클레임으로 빈 맵을 사용
        claims.put("phoneNumber", phoneNumber);
        return createToken(claims, userId, expirationTime); // 토큰 생성
    }

    // 클레임, 주체, 만료 시간을 기반으로 JWT 토큰을 생성하는 메서드
    private String createToken(Map<String, Object> claims, Long userId, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setSubject(String.valueOf(userId)) // 주체 설정 (유저 ID)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 시간으로 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()) // 서명 알고리즘과 서명 키 설정
                .compact(); // 최종 토큰을 생성하여 반환
    }

    // 토큰이 유효한지 검증하는 메서드 (사용자 이름과 만료 여부 확인)
    public Boolean validateToken(String token, String phoneNumber) {
        final String extractedUserId = extractUserId(token); // 토큰에서 사용자 ID 추출
        final String extractedPhoneNumber = extractPhoneNumber(token); // 토큰에서 사용자 전화번호 추출
        boolean matchedPhoneNumber = extractedPhoneNumber.equals(phoneNumber);
        return matchedPhoneNumber && !isTokenExpired(token) ; // 사용자 id와 전화번호가 일치하고 토큰이 만료되지 않았는지 확인
    }
}


