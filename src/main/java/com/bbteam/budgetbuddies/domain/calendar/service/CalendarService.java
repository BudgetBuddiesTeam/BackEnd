package com.bbteam.budgetbuddies.domain.calendar.service;

import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;

public interface CalendarService {

    CalendarDto.CalendarMonthResponseDto find(int year, int month);

}
