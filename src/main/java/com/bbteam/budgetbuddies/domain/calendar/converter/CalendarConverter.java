package com.bbteam.budgetbuddies.domain.calendar.converter;

import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;

import java.util.List;

public class CalendarConverter {

    public static CalendarDto.CalendarMonthResponseDto toCalendarMonthResponseDto(CalendarDto.CalendarMonthInfoDto calendarMonthInfoDto, CalendarDto.CalendarMonthInfoDto recommendMonthInfoDto){
        return CalendarDto.CalendarMonthResponseDto.builder()
                .calendarMonthInfoDto(calendarMonthInfoDto)
                .recommendMonthInfoDto(recommendMonthInfoDto)
                .build();
    }

    public static CalendarDto.CalendarMonthInfoDto toCalendarMonthInfoDto(List<DiscountInfo> discountInfoList,
                                                                          List<SupportInfo> supportInfoList) {
        List<CalendarDto.CalendarDiscountInfoDto> discountInfoDtoList = discountInfoList.stream()
                .map(CalendarConverter::toCalendarDiscountInfoDto)
                .toList();
        List<CalendarDto.CalendarSupportInfoDto> supportInfoDtoList = supportInfoList.stream()
                .map(CalendarConverter::toCalendarSupportInfoDto)
                .toList();

        return  CalendarDto.CalendarMonthInfoDto.builder()
                .discountInfoDtoList(discountInfoDtoList)
                .supportInfoDtoList(supportInfoDtoList)
                .build();
    }

    public static CalendarDto.CalendarDiscountInfoDto toCalendarDiscountInfoDto(DiscountInfo discountInfo) {
        return CalendarDto.CalendarDiscountInfoDto.builder()
                .id(discountInfo.getId())
                .title(discountInfo.getTitle())
                .likeCount(discountInfo.getLikeCount())
                .startDate(discountInfo.getStartDate())
                .endDate(discountInfo.getEndDate())
                .discountRate(discountInfo.getDiscountRate())
                .build();
    }

    public static CalendarDto.CalendarSupportInfoDto toCalendarSupportInfoDto(SupportInfo supportInfo) {
        return CalendarDto.CalendarSupportInfoDto.builder()
                .id(supportInfo.getId())
                .title(supportInfo.getTitle())
                .likeCount(supportInfo.getLikeCount())
                .startDate(supportInfo.getStartDate())
                .endDate(supportInfo.getEndDate())
                .build();
    }


}
