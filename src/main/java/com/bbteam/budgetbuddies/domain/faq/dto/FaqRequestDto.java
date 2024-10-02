package com.bbteam.budgetbuddies.domain.faq.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FaqRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FaqPostRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FaqModifyRequest {
        @NotBlank
        private String title;
        @NotBlank
        private String body;
    }
}
