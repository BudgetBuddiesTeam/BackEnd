package com.bbteam.budgetbuddies.global.security.auth.dto;


import com.bbteam.budgetbuddies.global.security.auth.validation.ValidPhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationRequest {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ToReceiveNumber {
        @ValidPhoneNumber // 전화번호 유효성 검증
        private String phoneNumber;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ToLogin {
        // 전화번호, 인증번호
        @ValidPhoneNumber // 전화번호 유효성 검증
        private String phoneNumber;

        private String otpNumber;
    }


}
