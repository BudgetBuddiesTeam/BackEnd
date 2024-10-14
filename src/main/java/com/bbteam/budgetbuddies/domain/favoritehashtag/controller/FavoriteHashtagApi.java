package com.bbteam.budgetbuddies.domain.favoritehashtag.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.favoritehashtag.dto.FavoriteHashtagResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FavoriteHashtagApi {

    @Operation(summary = "[User] 해당되는 해시태그를 설정한 유저 조회 API", description = "특정 할인정보 또는 지원정보에 등록된 해시태그를 설정한 유저를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "discountInfoId", description = "조회할 할인정보 ID", required = false),
            @Parameter(name = "supportInfoId", description = "조회할 지원정보 ID", required = false),
    })
    ApiResponse<List<FavoriteHashtagResponseDto>> getUsersByHashtags(
            @RequestParam(required = false) Long discountInfoId,
            @RequestParam(required = false) Long supportInfoId
    );
}
