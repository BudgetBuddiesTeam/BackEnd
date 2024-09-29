package com.bbteam.budgetbuddies.apiPayload.code.status;

import com.bbteam.budgetbuddies.apiPayload.code.BaseErrorCode;
import com.bbteam.budgetbuddies.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {


    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청"),
    _USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    _COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4001", "해당 댓글이 없습니다.") ,
    _NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTICE4001", "해당 공지가 없습니다."),
    _PAGE_LOWER_ZERO(HttpStatus.BAD_REQUEST, "PAGE4001", "요청된 페이지가 0보다 작습니다."),
    _TOKEN_NOT_VALID(HttpStatus.BAD_REQUEST, "TOKEN401", "토큰이 유효하지 않습니다."),
    _FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "FAQ4004", "해당 FAQ가 존재하지 않습니다.");


    private HttpStatus httpStatus;
    private String code;
    private String message;


    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
