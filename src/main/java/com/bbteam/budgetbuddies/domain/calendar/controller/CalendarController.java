package com.bbteam.budgetbuddies.domain.calendar.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController implements CalendarApi{

    private final CalendarService calendarService;

    @GetMapping("/")
    public ApiResponse<CalendarDto.CalendarMonthResponseDto> request(
            @RequestParam("year") Integer year, @RequestParam("month") Integer month
    ) {
        CalendarDto.CalendarMonthResponseDto result = calendarService.find(year, month);
        return ApiResponse.onSuccess(result);
    }
}
