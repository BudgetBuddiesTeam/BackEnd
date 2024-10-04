package com.bbteam.budgetbuddies.global.security2.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security2.JwtUtil;
import com.bbteam.budgetbuddies.global.security2.MyUserDetailsService;
import com.bbteam.budgetbuddies.global.security2.dto.AuthenticationRequest;
import com.bbteam.budgetbuddies.global.security2.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


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

//    @PostMapping("/login")
//    public ApiResponse<AuthenticationResponse.SendTokens> login(
//            @RequestBody AuthenticationRequest.ToLogin toLogin
//    ) {
//        // 사용자가 입력한 자격 증명으로 인증을 시도
//        /**
//         * 해당 로직은 ID,비밀번호를 사용하는 로직에서 사용한다고 합니다. 저희같이 OTP 사용할꺼면 사용 안해도 된데요 by gpt
//        Authentication authenticate = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
//        );
//
//         **/
//
//        /*
//            전화번호와 인증번호를 받고, 이를 인증하는 로직
//         */
//
//
//        // 인증이 성공하면 사용자 세부 정보를 로드
//        final UserDetails user = userDetailsService.loadUserByUsername(toLogin.getPhoneNumber());
//
//        Long userId = Long.parseLong(user.getPassword());
//        String phoneNumber = user.getUsername();
//        // AccessToken과 RefreshToken 생성
//        final String accessToken = jwtUtil.generateToken(userId, phoneNumber ,Long.parseLong(accessTokenExpirationTime)); // 1시간 유효
//        final String refreshToken = jwtUtil.generateToken(userId, phoneNumber, Long.parseLong(refreshTokenExpirationTime)); // 2주 유효
//
//        // 생성된 토큰을 포함한 응답을 반환
//        AuthenticationResponse.SendTokens response = AuthenticationResponse.SendTokens.builder()
//                .userId(userId)
//                .phoneNumber(phoneNumber)
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//
//        return ApiResponse.onSuccess(response);
//    }

    @PostMapping("/login/test")
    public ApiResponse<AuthenticationResponse.SendTokens> login(
            @RequestBody AuthenticationRequest.ToLogin toLogin
    ) {
        // 사용자가 입력한 자격 증명으로 인증을 시도
        /**
         * 해당 로직은 ID,비밀번호를 사용하는 로직에서 사용한다고 합니다. 저희같이 OTP 사용할꺼면 사용 안해도 된데요 by gpt
         Authentication authenticate = authenticationManager.authenticate(
         new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
         );

         **/

        /*
            전화번호와 인증번호를 받고, 이를 인증하는 로직
         */


        // 인증이 성공하면 사용자 세부 정보를 로드
        final UserDetails user = userDetailsService.loadUserByUsername(toLogin.getPhoneNumber());

        Long userId = Long.parseLong(user.getPassword());
        String phoneNumber = user.getUsername();
        // AccessToken과 RefreshToken 생성
        final String accessToken = jwtUtil.generateToken(userId, phoneNumber ,Long.parseLong(accessTokenExpirationTime)); // 1시간 유효
        final String refreshToken = jwtUtil.generateToken(userId, phoneNumber, Long.parseLong(refreshTokenExpirationTime)); // 2주 유효

        // 생성된 토큰을 포함한 응답을 반환
        AuthenticationResponse.SendTokens response = AuthenticationResponse.SendTokens.builder()
                .userId(userId)
                .phoneNumber(phoneNumber)
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

}