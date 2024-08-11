package com.bbteam.budgetbuddies.domain.calendar.controller;

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
public class CalendarController {

    private final CalendarService calendarService;
    @Operation(summary = "[User] 주머니 캘린더 API", description = "주머니 캘린더 화면에 필요한 API를 호출합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "year", description = "호출할 연도입니다."),
            @Parameter(name = "month", description = "호출할 연도의 월입니다."),
    })
    @GetMapping
    public ApiResponse<CalendarDto.CalendarMonthResponseDto> request(
            @RequestParam("year") Integer year, @RequestParam("month") Integer month
    ) {
        CalendarDto.CalendarMonthResponseDto result = calendarService.find(year, month);
        return ResponseEntity.ok(result);
    }
}
