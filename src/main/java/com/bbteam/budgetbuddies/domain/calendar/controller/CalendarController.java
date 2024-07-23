package com.bbteam.budgetbuddies.domain.calendar.controller;

import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/")
    public ResponseEntity<CalendarDto.CalendarMonthResponseDto> request(
            @RequestParam("year") Integer year, @RequestParam("month") Integer month
    ) {
        CalendarDto.CalendarMonthResponseDto result = calendarService.find(year, month);
        return ResponseEntity.ok(result);
    }
}
