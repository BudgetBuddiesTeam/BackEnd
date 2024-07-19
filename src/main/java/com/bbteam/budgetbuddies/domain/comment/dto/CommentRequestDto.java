package com.bbteam.budgetbuddies.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class CommentRequestDto {
    @Setter
    @Getter
    @Builder
    public static class DiscountInfoCommentDto {
        private String content;
        private Long discountInfoId;
    }

    @Setter
    @Getter
    @Builder
    public static class SupportInfoCommentDto {
        private String content;
        private Long supportInfoId;
    }
}
