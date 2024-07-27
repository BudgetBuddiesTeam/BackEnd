package com.bbteam.budgetbuddies.domain.mainpage.controller;

import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface MainPageApi {

    @Operation(summary = "[User] 메인페이지 요청 API", description = "userId 기반으로 메인 페이지를 가져옵니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "현재 데이터를 요청하는 사용자입니다. parameter"),
    })
    ResponseEntity<MainPageResponseDto> getMainPage(Long userId);
}
