package com.bbteam.budgetbuddies.apiPayload.code.status;

import com.bbteam.budgetbuddies.apiPayload.code.BaseErrorCode;
import com.bbteam.budgetbuddies.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {


    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버에러"),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수입니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT4001", "해당 댓글이 없습니다.") ,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4002", "게시글이 없습니다."),
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "test"),
    FOOD_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "FOOD_CATEGORY4004", "해당하는 음식 카테고리가 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE4004", "해당하는 가게는 존재하지 않습니다."),
    MISSION_ALREADY_ACCEPT(HttpStatus.BAD_REQUEST, "MISSION4001", "해당 미션은 이미 수주되었습니다."),
    REVIEW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "REVIEW4001","해당 가게에 리뷰를 이미 작성하셨습니다."),
    PAGE_LOWER_ZERO(HttpStatus.BAD_REQUEST, "PAGE4001", "요청된 페이지가 0보다 작습니다."),
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION4004", "해당 미션이 존재하지 않습니다."),
    MISSION_ALREADY_COMPLETE(HttpStatus.BAD_REQUEST, "MISSION4001", "해당 미션은 이미 완료된 미션입니다.");


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
