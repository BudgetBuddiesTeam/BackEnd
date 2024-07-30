package com.bbteam.budgetbuddies.domain.comment.dto;


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
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class DiscountInfoSuccessDto{
        private Long commentId;
        private Long userId;
        private Long discountInfoId;
        private String content;
    }

    @Getter
    @Builder
    public static class SupportInfoSuccessDto{
        private Long commentId;
        private Long userId;
        private Long supportInfoId;
        private String content;
    }

}
