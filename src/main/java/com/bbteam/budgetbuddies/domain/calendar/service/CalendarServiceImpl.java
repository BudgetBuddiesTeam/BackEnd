package com.bbteam.budgetbuddies.domain.calendar.service;

import com.bbteam.budgetbuddies.domain.calendar.converter.CalendarConverter;
import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService{

    private final DiscountInfoRepository discountInfoRepository;

    private final SupportInfoRepository supportInfoRepository;

    public CalendarDto.CalendarMonthResponseDto find(int year, int month){
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        CalendarDto.CalendarMonthResponseDto result = getCalendarMonthResponseDto(firstDay, lastDay);
        return result;
    }

    private CalendarDto.CalendarMonthResponseDto getCalendarMonthResponseDto(LocalDate firstDay, LocalDate lastDay) {
        CalendarDto.CalendarMonthInfoDto calendarMonthInfoDto = getCalendarMonthInfoDto(firstDay, lastDay);
        CalendarDto.CalendarMonthInfoDto recommendMonthInfoDto = getRecommendMonthInfoDto(firstDay, lastDay);
        return CalendarConverter.toCalendarMonthResponseDto(calendarMonthInfoDto, recommendMonthInfoDto);
    }

    private CalendarDto.CalendarMonthInfoDto getCalendarMonthInfoDto(LocalDate firstDay, LocalDate lastDay) {
        List<DiscountInfo> monthDiscountInfoList = discountInfoRepository.findByMonth(firstDay, lastDay);
        List<SupportInfo> monthSupportInfoList = supportInfoRepository.findByMonth(firstDay, lastDay);
        return CalendarConverter.toCalendarMonthInfoDto(monthDiscountInfoList, monthSupportInfoList);
    }

    private CalendarDto.CalendarMonthInfoDto getRecommendMonthInfoDto(LocalDate firstDay, LocalDate lastDay) {
        List<DiscountInfo> recommendDiscountInfoList = discountInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);
        List<SupportInfo> recommendSupportInfoList = supportInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);
        return CalendarConverter.toCalendarMonthInfoDto(recommendDiscountInfoList, recommendSupportInfoList);
    }
}
