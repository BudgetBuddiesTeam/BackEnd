package com.bbteam.budgetbuddies.global.security.otp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpNumber {

    private String otp; // 인증번호

    private LocalDateTime expirationTime; // 만료시각

}
