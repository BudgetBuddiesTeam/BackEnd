package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface ConsumptionGoalApi {
	@Operation(summary = "또래들이 가장 큰 계획을 세운 카테고리 조회 API", description = "특정 사용자의 소비 목표 카테고리별 소비 목표 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({@Parameter(name = "top", description = "가장 큰 목표를 세운 카테고리의 개수를 지정합니다. (기본값은 5입니다)"),
		@Parameter(name = "userId", description = "로그인 한 유저 아이디"),
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ResponseEntity<?> getTopGoalCategories(@RequestParam(name = "top", defaultValue = "5") int top, Long userId,
		int peerAgeStart, int peerAgeEnd, String peerGender);

	@Operation(summary = "소비 목표 조회 API", description = "date={yyyy-MM-dd} 형식의 query string을 통해서 사용자의 목표 달을 조회하는 API 입니다.")
	@Parameters({@Parameter(name = "date", description = "yyyy-MM-dd 형식으로 목표 달의 소비를 조회")})
	ResponseEntity<ConsumptionGoalResponseListDto> findUserConsumptionGoal(LocalDate date, Long userId);

	@Operation(summary = "또래나이와 성별 조회 API", description = "또래나이와 성별을 조회하는 API 입니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({@Parameter(name = "userId", description = "로그인 한 유저 아이디"),
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ResponseEntity<?> getPeerInfo(Long userId, int peerAgeStart, int peerAgeEnd, String peerGender);

	@Operation(summary = "이번 달 소비 목표 수정 API", description = "다른 달의 소비 목표를 업데이트하는 것은 불가능하고 오직 이번 달의 소비 목표만 업데이트 하는 API 입니다.")
	ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(Long userId,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto);
}
