package com.bbteam.budgetbuddies.domain.calendar.service;

import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;

public interface CalendarService {

    public CalendarDto.CalendarMonthResponseDto find(int year, int month);

}
