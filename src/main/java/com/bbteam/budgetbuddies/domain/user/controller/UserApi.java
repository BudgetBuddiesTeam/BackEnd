package com.bbteam.budgetbuddies.domain.user.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserApi {
    @Operation(summary = "[Admin] 기본 카테고리 생성 API ", description = "기본 카테고리가 없는 사용자에게 기본 카테고리를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "기본 카테고리를 만들 사용자 ID 입니다. pathVariable",
                    in = ParameterIn.PATH),
    })
    ResponseEntity<List<UserConsumptionGoalResponse>> createConsumptionGoals(@PathVariable Long userId);

    @Operation(summary = "[Admin] User 추가 API ", description = "서비스를 이용할 사용자를 추가합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "phoneNumber", description = "휴대폰 번호. requestBody"),
            @Parameter(name = "name", description = "사용자 이름. requestBody"),
            @Parameter(name = "age", description = "사용자 나이. requestBody"),
            @Parameter(name = "gender", description = "사용자 성별 / MALE, FEMALE, NONE requestBody"),
            @Parameter(name = "email", description = "메일 주소. requestBody"),
            @Parameter(name = "photoUrl", description = "사진 Url. 아마 사용 x requestBody"),
            @Parameter(name = "consumptionPattern", description = "소비 패턴. 아마 사용 x requestBody")
    })
    ApiResponse<UserDto.ResponseUserDto> registerUser(@RequestBody UserDto.RegisterUserDto dto);
    @Operation(summary = "[Admin] User 찾기 ", description = "ID를 기반으로 해당 사용자를 찾습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "찾고자하는 사용자 id 입니다. pathVariable",
                    in = ParameterIn.PATH)
    })
    ApiResponse<UserDto.ResponseUserDto> findOne(@PathVariable("userId") @ExistUser Long userId);

    @Operation(summary = "[Admin] 모든 User정보 찾기 ", description = "DB에 존재하는 모든 user 정보를 가져옵니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    ApiResponse<List<UserDto.ResponseUserDto>> findAll();

    @Operation(summary = "[Admin] User 데이터 변경 ", description = "사용자의 email, name을 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "변경하고자하는 사용자 id 입니다. pathVariable",
            in = ParameterIn.PATH),
            @Parameter(name = "email", description = "변경할 사용자 email 주소입니다. requestBody"),
            @Parameter(name = "name", description = "변경할 사용자 name 입니다. requestBody")

    })
    ApiResponse<UserDto.ResponseUserDto> changeOne(@PathVariable("userId") @ExistUser Long userId,
                                                   @RequestBody UserDto.ModifyUserDto dto);
}
