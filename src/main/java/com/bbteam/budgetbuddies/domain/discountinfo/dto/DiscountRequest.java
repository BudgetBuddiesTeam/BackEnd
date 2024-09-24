package com.bbteam.budgetbuddies.domain.discountinfo.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class DiscountRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterDiscountDto {

        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer discountRate;

        private String siteUrl;

        private String thumbnailUrl;

        private Boolean isInCalendar;

        private List<Long> hashtagIds;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDiscountDto {

        private Long id;

        private String title;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer discountRate;

        private String siteUrl;

        private String thumbnailUrl;

        private Boolean isInCalendar;

        private List<Long> hashtagIds;

    }


}
