package com.bbteam.budgetbuddies.domain.comment.dto;


import lombok.Builder;
import lombok.Getter;

public class CommentResponseDto {

    @Getter
    @Builder
    public static class DiscountInfoCommentDto{
        private Long commentId;
        private Long userId;
        private Long discountInfoId;
        private String content;
        private Integer anonymousNumber;
    }

    @Getter
    @Builder
    public static class SupportInfoCommentDto{
        private Long commentId;
        private Long userId;
        private Long supportInfoId;
        private String content;
        private Integer anonymousNumber;
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
