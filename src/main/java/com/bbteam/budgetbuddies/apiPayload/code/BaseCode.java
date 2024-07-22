package com.bbteam.budgetbuddies.apiPayload.code;

public interface BaseCode {
    ReasonHttpStatus getReasonHttpStatus();

    interface ReasonHttpStatus {
        String getCode();
        String getMessage();
    }
}
