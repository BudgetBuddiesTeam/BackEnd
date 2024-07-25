package com.bbteam.budgetbuddies.apiPayload.code.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus {
    OK("200", "OK");

    private final String code;
    private final String message;
}

