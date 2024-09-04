package com.bbteam.budgetbuddies.domain.category.controller;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface CategoryApi {

    @Operation(summary = "[User] 카테고리 추가", description = "사용자가 직접 카테고리를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/add/{userId}")
    ResponseEntity<CategoryResponseDto> createCategory(
            @PathVariable Long userId,
            @Parameter(description = "user_id, name(사용자가 입력한 카테고리명), is_default(default 카테고리 여부)로 request")
            @RequestBody CategoryRequestDto categoryRequestDTO);


    @Operation(summary = "[User] 유저 개인의 카테고리 조회", description = "유저의 카테고리(default + 개인 custom)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/get/{userId}")
    ResponseEntity<List<CategoryResponseDto>> getUserCategories(@PathVariable Long userId);

    @Operation(summary = "[User] 커스텀 카테고리 삭제", description = "특정 카테고리를 삭제하는 API이며, 사용자의 ID를 입력하여 사용합니다. (추후 토큰으로 검증)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "userId", description = "해당하는 사용자의 id"),
            @Parameter(name = "categoryId", description = "삭제할 카테고리의 id"),
    })
    ResponseEntity<String> deleteCategory(
            @RequestParam Long userId,
            @PathVariable Long categoryId
    );
}
