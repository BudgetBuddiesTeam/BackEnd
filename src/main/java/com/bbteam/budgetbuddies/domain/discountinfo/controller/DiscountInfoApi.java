package com.bbteam.budgetbuddies.domain.discountinfo.controller;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface DiscountInfoApi {

    @Operation(summary = "[User] 특정 년월 할인정보 리스트 가져오기 API", description = "특정 년도와 월에 해당하는 할인정보 목록을 조회하는 API이며, 페이징을 포함합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "year", description = "데이터를 가져올 연도입니다."),
        @Parameter(name = "month", description = "데이터를 가져올 월입니다."),
        @Parameter(name = "page", description = "페이지 번호, 0번이 1 페이지 입니다. (기본값은 0입니다.)"),
        @Parameter(name = "size", description = "한 페이지에 불러올 데이터 개수입니다. (기본값은 10개입니다.)")
    })
    ResponseEntity<Page<DiscountResponseDto>> getDiscountsByYearAndMonth(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size
    );

    @Operation(summary = "[ADMIN] 할인정보 등록하기 API", description = "할인정보를 등록하는 API이며, 추후에는 관리자만 접근 가능하도록 할 예정입니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
//        @Parameter(name = "discountRequestDto", description = "등록할 할인 정보의 전체 내용입니다."),
    })
    public ResponseEntity<DiscountResponseDto> registerDiscountInfo(
        @RequestBody DiscountRequest.RegisterDto discountRequestDto
    );

    @Operation(summary = "[User] 특정 할인정보에 좋아요 클릭 API", description = "특정 할인정보에 좋아요 버튼을 클릭하는 API이며, 일단은 사용자 ID를 입력하여 사용합니다. (추후 토큰으로 검증)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "userId", description = "좋아요를 누른 사용자의 id입니다."),
        @Parameter(name = "discountInfoId", description = "좋아요를 누를 할인정보의 id입니다."),
    })
    public ResponseEntity<DiscountResponseDto> likeDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long discountInfoId
    );

    @Operation(summary = "[ADMIN] 특정 할인정보 수정하기 API", description = "특정 할인정보를 수정하는 API이며, 일단은 사용자 ID를 입력하여 사용합니다. (추후 토큰으로 검증)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "userId", description = "수정할 사용자의 id입니다."),
//        @Parameter(name = "discountRequestDto", description = "수정할 할인 정보의 전체 내용입니다."),
    })
    public ResponseEntity<DiscountResponseDto> updateDiscountInfo(
        @RequestParam Long userId,
        @RequestBody DiscountRequest.UpdateDto discountRequestDto
    );

    @Operation(summary = "[ADMIN] 특정 할인정보 삭제하기 API", description = "특정 할인정보를 삭제하는 API이며, 일단은 사용자 ID를 입력하여 사용합니다. (추후 토큰으로 검증)")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
        @Parameter(name = "userId", description = "삭제할 사용자의 id입니다."),
        @Parameter(name = "discountInfoId", description = "삭제할 할인 정보의 id입니다."),
    })
    public ResponseEntity<String> deleteDiscountInfo(
        @RequestParam Long userId,
        @PathVariable Long discountInfoId
    );

}
