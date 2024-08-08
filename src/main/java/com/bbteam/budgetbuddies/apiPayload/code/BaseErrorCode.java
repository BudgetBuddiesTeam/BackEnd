package com.bbteam.budgetbuddies.apiPayload.code;

public interface BaseErrorCode {

    ErrorReasonDto getReason();

    ErrorReasonDto getReasonHttpStatus();
}
