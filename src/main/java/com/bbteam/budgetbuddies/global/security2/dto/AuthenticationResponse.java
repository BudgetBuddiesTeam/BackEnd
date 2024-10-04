package com.bbteam.budgetbuddies.global.security2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendCertificationNumber { // AuthenticationRequest.ToReceiveNumber와 대응
        // 전화번호, 인증번호 객체
        private String phoneNumber;

        private CertificationNumber certificationNumber;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendTokens { // AuthenticationRequest.ToLogin과 대응
        // 유저 아이디, 전화번호, 액세스 토큰, 리프레시 토큰
        private Long userId;

        private String phoneNumber;

        private String accessToken;

        private String refreshToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAccessToken { // AuthenticationRequest.ToReissueAccessToken와 대응
        // 유저 아이디, 전화번호, 액세스 토큰
        private Long userId;

        private String phoneNumber;

        private String accessToken;

    }


}
