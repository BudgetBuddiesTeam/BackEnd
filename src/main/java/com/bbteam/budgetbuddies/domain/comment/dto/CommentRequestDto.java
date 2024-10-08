package com.bbteam.budgetbuddies.domain.comment.dto;

import com.bbteam.budgetbuddies.domain.comment.validation.ExistComment;
import lombok.Builder;
import lombok.Getter;


public class CommentRequestDto {
    @Getter
    @Builder
    public static class DiscountInfoCommentRequestDto {
        private String content;
        private Long discountInfoId;
    }

    @Getter
    @Builder
    public static class SupportInfoCommentRequestDto {
        private String content;
        private Long supportInfoId;
    }

    @Getter
    @Builder
    public static class CommentModifyRequestDto {
        private String content;

        @ExistComment
        private Long commentId;
    }
}
