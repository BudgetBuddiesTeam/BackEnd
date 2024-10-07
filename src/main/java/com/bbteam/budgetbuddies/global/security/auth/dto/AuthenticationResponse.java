package com.bbteam.budgetbuddies.global.security.auth.dto;

import com.bbteam.budgetbuddies.global.security.otp.OtpNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthenticationResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "OTP 요청에 대한 응답 DTO")
    public static class SendOtpNumber { // AuthenticationRequest.ToReceiveNumber와 대응

        @Schema(description = "전화번호", example = "01012341234")
        private String phoneNumber; // 전화번호

        @Schema(description = "생성된 OTP 정보")
        private OtpNumber otpNumber; // 인증번호 객체
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 요청에 대한 응답 DTO")
    public static class SendTokens { // AuthenticationRequest.ToLogin과 대응

        @Schema(description = "유저 ID")
        private Long userId; // 유저 아이디

        @Schema(description = "전화번호", example = "01012341234")
        private String phoneNumber; // 전화번호

        @Schema(description = "액세스 토큰")
        private String accessToken; // 액세스 토큰

        @Schema(description = "리프레시 토큰")
        private String refreshToken; // 리프레시 토큰
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "액세스 토큰 재발급 요청에 대한 응답 DTO")
    public static class SendAccessToken {

        @Schema(description = "유저 ID")
        private Long userId; // 유저 아이디

        @Schema(description = "전화번호", example = "01012341234")
        private String phoneNumber; // 전화번호

        @Schema(description = "액세스 토큰")
        private String accessToken; // 액세스 토큰
    }
}