package com.bbteam.budgetbuddies.domain.calendar.converter;

import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;

import java.time.LocalDate;
import java.util.List;

public class CalendarConverter {

    public static CalendarDto.CalendarMonthResponseDto toCalendarMonthResponseDto(CalendarDto.CalendarMonthInfoDto calendarMonthInfoDto, CalendarDto.CalendarMonthInfoDto recommendMonthInfoDto){
        return CalendarDto.CalendarMonthResponseDto.builder()
                .calendarMonthInfoDto(calendarMonthInfoDto)
                .recommendMonthInfoDto(recommendMonthInfoDto)
                .build();
    }

    public static CalendarDto.CalendarMonthInfoDto toCalendarMonthInfoDto(List<DiscountInfo> discountInfoList,
                                                                          List<SupportInfo> supportInfoList,
                                                                          LocalDate firstDay, LocalDate lastDay) {
        List<CalendarDto.CalendarDiscountInfoDto> discountInfoDtoList = discountInfoList.stream()
                .map(discountInfo -> toCalendarDiscountInfoDto(discountInfo, firstDay, lastDay))
                .toList();
        List<CalendarDto.CalendarSupportInfoDto> supportInfoDtoList = supportInfoList.stream()
                .map(supportInfo -> toCalendarSupportInfoDto(supportInfo, firstDay, lastDay))
                .toList();

        return  CalendarDto.CalendarMonthInfoDto.builder()
                .discountInfoDtoList(discountInfoDtoList)
                .supportInfoDtoList(supportInfoDtoList)
                .build();
    }

    public static CalendarDto.CalendarDiscountInfoDto toCalendarDiscountInfoDto(DiscountInfo discountInfo,
                                                                                LocalDate firstDay, LocalDate lastDay) {
        LocalDate startDate = null;
        LocalDate endDate = null;

        if(discountInfo.getStartDate().isBefore(firstDay)) {
            startDate = firstDay;
        } else {
            startDate = discountInfo.getStartDate();
        }

        if(discountInfo.getEndDate().isAfter(lastDay)) {
            endDate = lastDay;
        } else {
            endDate = discountInfo.getEndDate();
        }


        return CalendarDto.CalendarDiscountInfoDto.builder()
                .id(discountInfo.getId())
                .title(discountInfo.getTitle())
                .likeCount(discountInfo.getLikeCount())
                .startDate(startDate)
                .endDate(endDate)
                .discountRate(discountInfo.getDiscountRate())
                .siteUrl(discountInfo.getSiteUrl())
                .thumbnailURL(discountInfo.getThumbnailUrl())
                .build();
    }

    public static CalendarDto.CalendarSupportInfoDto toCalendarSupportInfoDto(SupportInfo supportInfo,
                                                                                LocalDate firstDay, LocalDate lastDay) {

        LocalDate startDate = null;
        LocalDate endDate = null;

        if(supportInfo.getStartDate().isBefore(firstDay)) {
            startDate = firstDay;
        } else {
            startDate = supportInfo.getStartDate();
        }

        if(supportInfo.getEndDate().isAfter(lastDay)) {
            endDate = lastDay;
        } else {
            endDate = supportInfo.getEndDate();
        }

        return CalendarDto.CalendarSupportInfoDto.builder()
                .id(supportInfo.getId())
                .title(supportInfo.getTitle())
                .likeCount(supportInfo.getLikeCount())
                .startDate(startDate)
                .endDate(endDate)
                .siteUrl(supportInfo.getSiteUrl())
                .thumbnailURL(supportInfo.getThumbnailUrl())
                .build();
    }


}
