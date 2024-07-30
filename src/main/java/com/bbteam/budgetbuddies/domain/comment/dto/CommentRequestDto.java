package com.bbteam.budgetbuddies.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;


public class CommentRequestDto {
    @Getter
    @Builder
    public static class DiscountInfoCommentDto {
        private String content;
        private Long discountInfoId;
    }

    @Getter
    @Builder
    public static class SupportInfoCommentDto {
        private String content;
        private Long supportInfoId;
    }

    @Getter
    @Builder
    public static class CommentModifyDto {
        private String content;
        private Long commentId;
    }
}
