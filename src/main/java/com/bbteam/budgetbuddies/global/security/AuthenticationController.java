package com.bbteam.budgetbuddies.global.security;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager; // 인증을 처리하는 매니저

    @Autowired
    private JwtUtil jwtUtil; // JWT 유틸리티 클래스

    @Autowired
    private MyUserDetailsService userDetailsService; // 사용자 세부 정보 서비스

    @Value("${jwt.token.access-token.expiration-time}")
    private String accessTokenExpirationTime; // 엑세스 토큰 만료 기한

    @Value("${jwt.token.refresh-token.expiration-time}")
    private String refreshTokenExpirationTime; // 리프레시 토큰 만료 기한


    @PostMapping("/get-certification-number")
    public ApiResponse<AuthenticationResponse.SendCertificationNumber> getCertificationNumber(
            @RequestBody AuthenticationRequest.ToReceiveNumber toReceiveNumber
    ) {

        return null;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse.SendTokens> login(
            @RequestBody AuthenticationRequest.ToLogin toLogin
    ) {
        // 사용자가 입력한 자격 증명으로 인증을 시도
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        authenticate.getDetails();

        // 인증이 성공하면 사용자 세부 정보를 로드
        final User user = userDetailsService.loadUserByPhoneNumber(toLogin.getPhoneNumber());

        // AccessToken과 RefreshToken 생성
        final String accessToken = jwtUtil.generateToken(user, Long.parseLong(accessTokenExpirationTime)); // 1시간 유효
        final String refreshToken = jwtUtil.generateToken(user, Long.parseLong(refreshTokenExpirationTime)); // 2주 유효

        // 생성된 토큰을 포함한 응답을 반환
        AuthenticationResponse.SendTokens response = AuthenticationResponse.SendTokens.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/reissue-access-token")
    public ApiResponse<AuthenticationResponse.SendAccessToken> reIssueAccessToken(
            @RequestBody AuthenticationRequest.ToReissueAccessToken request
    ) {
        return null;
    }


    // 사용자 로그인 요청을 처리하는 메서드
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
    ) throws Exception {
        // 사용자가 입력한 자격 증명으로 인증을 시도
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );

        // 인증이 성공하면 사용자 세부 정보를 로드
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // AccessToken과 RefreshToken 생성
        final String accessToken = jwtUtil.generateToken(userDetails.getUsername(), Long.parseLong(accessTokenExpirationTime)); // 1시간 유효
        final String refreshToken = jwtUtil.generateToken(userDetails.getUsername(), Long.parseLong(refreshTokenExpirationTime)); // 1년 유효

        // 생성된 토큰을 포함한 응답을 반환

        return ;
    }
}

