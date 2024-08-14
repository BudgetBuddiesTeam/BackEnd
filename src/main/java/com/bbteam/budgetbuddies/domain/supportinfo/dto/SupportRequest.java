package com.bbteam.budgetbuddies.domain.supportinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public class SupportRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterSupportDto {

        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private String siteUrl;

        private String thumbnailUrl;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSupportDto {

        private Long id;

        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private String siteUrl;

        private String thumbnailUrl;

    }


}
