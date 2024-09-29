package com.bbteam.budgetbuddies.global.security;


import lombok.*;

public class AuthenticationRequest {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ToReceiveNumber {
        // 전화번호
        private String phoneNumber;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ToLogin {
        // 전화번호, 인증번호
        private String phoneNumber;

        private String certificationNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ToReissueAccessToken {
        // 전화번호, 리프레시 토큰
        private String phoneNumber;

        private String RefreshToken;
    }


}
