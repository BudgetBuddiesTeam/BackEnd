package com.bbteam.budgetbuddies.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@Getter
public class ReasonDto {
    String message;
    String code;
    Boolean isSuccess;
    HttpStatus httpStatus;
}
