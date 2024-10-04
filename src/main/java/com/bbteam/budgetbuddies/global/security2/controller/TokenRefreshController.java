package com.bbteam.budgetbuddies.global.security2.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.global.security2.dto.AuthenticationRequest;
import com.bbteam.budgetbuddies.global.security2.dto.AuthenticationResponse;
import com.bbteam.budgetbuddies.global.security2.JwtUtil;
import com.bbteam.budgetbuddies.global.security2.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security2")
public class TokenRefreshController {

    @Autowired
    private JwtUtil jwtUtil; // JWT 유틸리티 클래스

    @Value("${jwt.token.access-token.expiration-time}")
    private String accessTokenExpirationTime; // 엑세스 토큰 만료 기한

    @Autowired
    private MyUserDetailsService userDetailsService; // 사용자 세부 정보 서비스

    // RefreshToken을 사용해 AccessToken을 갱신하는 메서드
    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse.SendAccessToken> refreshAuthenticationToken(@RequestBody AuthenticationRequest.ToReissueAccessToken request) {
        String refreshToken = request.getRefreshToken(); // 요청에서 RefreshToken 추출

        String phoneNumber = jwtUtil.extractPhoneNumber(refreshToken); // RefreshToken에서 사용자 이름 추출
        UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber); // 사용자 세부 정보 로드
        Long userId = Long.parseLong(userDetails.getPassword());
        // RefreshToken이 유효한지 검증
        if (jwtUtil.validateToken(refreshToken, userDetails.getUsername())) {
            String newAccessToken = jwtUtil.generateToken(userId,  phoneNumber, Long.parseLong(accessTokenExpirationTime)); // 새로운 AccessToken 생성 (1시간 유효)
            return ApiResponse.onSuccess(AuthenticationResponse.SendAccessToken.builder()
                    .accessToken(newAccessToken)
                    .userId(userId)
                    .phoneNumber(phoneNumber)
                    .build());// 새로운 AccessToken 반환 (RefreshToken은 재사용)
        } else {
            throw new GeneralException(ErrorStatus._TOKEN_NOT_VALID); // RefreshToken이 유효하지 않으면 예외 발생
        }
    }
}
