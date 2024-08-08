package com.bbteam.budgetbuddies.domain.comment.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommentResponseDto {

    @Getter
    @Builder
    public static class DiscountInfoCommentDto{
        private Long commentId;
        private Long userId;
        private Long discountInfoId;
        private String content;
        private Integer anonymousNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class SupportInfoCommentDto{
        private Long commentId;
        private Long userId;
        private Long supportInfoId;
        private String content;
        private Integer anonymousNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
    }



}
