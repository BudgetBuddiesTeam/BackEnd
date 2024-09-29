package com.bbteam.budgetbuddies.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationNumber {

    private String number; // 인증번호

    private Integer effectiveTime; // 유효시간 (ex. 3분 = 3)

    private LocalDateTime createdAt; // 생성시각


}
