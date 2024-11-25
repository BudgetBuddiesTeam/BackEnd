package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AllConsumptionCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MonthReportResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface ConsumptionGoalApi {

	@Operation(summary = "[User] 또래들이 가장 큰 계획을 세운 카테고리 조회 Top4", description = "특정 사용자의 소비 목표 카테고리별 소비 목표 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ApiResponse<List<TopGoalCategoryResponseDto>> getTopConsumptionGoalCategories(
		UserDto.AuthUserDto user, int peerAgeStart, int peerAgeEnd, String peerGender);

	@Operation(summary = "[User] 또래들이 가장 많이 계획한 카테고리와 평균 금액 및 내 목표금액 차이 조회", description = "특정 사용자의 또래 소비 카테고리별 평균 목표 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionGoalCategories(UserDto.AuthUserDto user,
		int peerAgeStart,
		int peerAgeEnd, String peerGender);

	@Operation(summary = "[User] 또래나이와 성별 조회", description = "또래나이와 성별을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ApiResponse<PeerInfoResponseDto> getPeerInfo(UserDto.AuthUserDto user, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	@Operation(summary = "[User] 소비 목표 조회", description = "date={yyyy-MM-dd} 형식의 query string을 통해서 사용자의 목표 달을 조회하는 API 입니다.")
	@Parameters({@Parameter(name = "date", description = "yyyy-MM-dd 형식으로 목표 달의 소비를 조회")})
	ApiResponse<ConsumptionGoalResponseListDto> findUserConsumptionGoal(LocalDate date, UserDto.AuthUserDto user);

	@Operation(summary = "[User] 이번 달 소비 목표 수정", description = "다른 달의 소비 목표를 업데이트하는 것은 불가능하고 오직 이번 달의 소비 목표만 업데이트 하는 API 입니다.")
	ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(UserDto.AuthUserDto user,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto);

	@Operation(summary = "[User] 또래들이 가장 많이한 소비 카테고리 조회 Top3", description = "특정 사용자의 또래 소비 카테고리별 소비 건 수을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ApiResponse<List<TopCategoryConsumptionDto>> getTopConsumptionCategories(UserDto.AuthUserDto user, int peerAgeStart,
		int peerAgeEnd, String peerGender);

	@Operation(summary = "[User] 또래들이 가장 많이한 소비 카테고리와 평균 금액 및 내 소비금액 차이 조회", description = "특정 사용자의 또래 소비 카테고리별 평균 소비 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위"),
		@Parameter(name = "peerAgeEnd", description = "또래나이 끝 범위"),
		@Parameter(name = "peerGender", description = "또래 성별")})
	ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionCategories(UserDto.AuthUserDto user,
		int peerAgeStart,
		int peerAgeEnd, String peerGender);

	@Operation(summary = "[User] 또래들이 가장 큰 목표로 세운 카테고리와 그 카테고리에서 이번주 사용한 금액 조회", description = "특정 사용자의 또래 소비 카테고리별 이번주 소비 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	ApiResponse<ConsumptionAnalysisResponseDto> getTopCategoryAndConsumptionAmount(UserDto.AuthUserDto user);

	@Operation(summary = "[User] 이번 달 레포트 표정, 멘트 조회 ", description = "특정 사용자의 이번 달 레포트 표정, 멘트를 조회하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	ApiResponse<MonthReportResponseDto> getMonthReport(UserDto.AuthUserDto user);

	@Operation(summary = "[User] 소비 분석 멘트 생성", description = "특정 사용자의 소비 분석 멘트를 생성 하는 API 입니다.")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	ApiResponse<String> getConsumptionMention(UserDto.AuthUserDto user) throws ExecutionException, InterruptedException;
}