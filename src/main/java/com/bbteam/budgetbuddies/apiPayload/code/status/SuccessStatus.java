package com.bbteam.budgetbuddies.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    _OK(HttpStatus.OK, "200", "okay");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
