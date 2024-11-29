package com.bbteam.budgetbuddies.domain.mainpage.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface MainPageApi {

    @Operation(summary = "[User] 메인페이지 요청 API", description = "userId 기반으로 메인 페이지를 가져옵니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    ApiResponse<MainPageResponseDto> getMainPage(@AuthUser UserDto.AuthUserDto userDto);
}
