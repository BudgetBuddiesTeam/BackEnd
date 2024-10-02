package com.bbteam.budgetbuddies.domain.faq.dto;

import lombok.Builder;
import lombok.Getter;

public class FaqResponseDto {

    // User
    @Getter
    @Builder
    public static class FaqPostResponse {
        private Long faqId;
        private String username;
        private String title;
        private String body;
    }

    //User
    @Getter
    @Builder
    public static class FaqModifyResponse {
        private Long faqId;
        private String username;
        private String title;
        private String body;
    }

    //Admin
    @Getter
    @Builder
    public static class FaqFindResponse {
        private Long faqId;
        private Long userId;
        private String username;
        private String title;
        private String body;
    }
}
