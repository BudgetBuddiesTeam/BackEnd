package com.bbteam.budgetbuddies.domain.calendar.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class CalendarDto {

    @Getter
    @Builder
    public static class CalendarMonthResponseDto {
        CalendarMonthInfoDto calendarMonthInfoDto;
        CalendarMonthInfoDto recommendMonthInfoDto;
    }

    @Getter
    @Builder
    public static class CalendarMonthInfoDto {
        private List<CalendarDiscountInfoDto> discountInfoDtoList;
        private List<CalendarSupportInfoDto> supportInfoDtoList;
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    public static class CalendarDiscountInfoDto {
        private Long id;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer likeCount;
        private Integer discountRate;
        private String siteUrl;
        private String thumbnailURL;
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    public static class CalendarSupportInfoDto {
        private Long id;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer likeCount;
        private String siteUrl;
        private String thumbnailURL;
    }
}
