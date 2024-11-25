package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AllConsumptionCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MonthReportResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption-goals")
public class ConsumptionGoalController implements ConsumptionGoalApi {

	private final ConsumptionGoalService consumptionGoalService;

	@Override
	@GetMapping("/categories/top-goals/top-4")
	public ApiResponse<List<TopGoalCategoryResponseDto>> getTopConsumptionGoalCategories(
		@AuthUser UserDto.AuthUserDto user,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopGoalCategoryResponseDto> response = consumptionGoalService.getTopConsumptionGoalCategories(
			user.getId(), peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/categories/top-goals")
	public ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionGoalCategories(
		@AuthUser UserDto.AuthUserDto user,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<AllConsumptionCategoryResponseDto> response = consumptionGoalService.getAllConsumptionGoalCategories(
			user.getId(),
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/peer-info")
	public ApiResponse<PeerInfoResponseDto> getPeerInfo(
		@AuthUser UserDto.AuthUserDto user,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		PeerInfoResponseDto response = consumptionGoalService.getPeerInfo(user.getId(), peerAgeStart, peerAgeEnd,
			peerGender);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping()
	public ApiResponse<ConsumptionGoalResponseListDto> findUserConsumptionGoal(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @AuthUser UserDto.AuthUserDto user) {

		return ApiResponse.onSuccess(consumptionGoalService.findUserConsumptionGoalList(user.getId(), date));
	}

	@Override
	@PostMapping()
	public ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(
		@AuthUser UserDto.AuthUserDto user,
		@RequestBody ConsumptionGoalListRequestDto consumptionGoalListRequestDto) {

		return ResponseEntity.ok()
			.body(consumptionGoalService.updateConsumptionGoals(user.getId(), consumptionGoalListRequestDto));
	}

	@Override
	@GetMapping("/categories/top-consumptions/top-3")
	public ApiResponse<List<TopCategoryConsumptionDto>> getTopConsumptionCategories(
		@AuthUser UserDto.AuthUserDto user,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopCategoryConsumptionDto> response = consumptionGoalService.getTopConsumptionCategories(user.getId(),
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/categories/top-consumptions")
	public ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionCategories(
		@AuthUser UserDto.AuthUserDto user,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<AllConsumptionCategoryResponseDto> response = consumptionGoalService.getAllConsumptionCategories(
			user.getId(),
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/category/top-goals")
	public ApiResponse<ConsumptionAnalysisResponseDto> getTopCategoryAndConsumptionAmount(
		@AuthUser UserDto.AuthUserDto user) {
		ConsumptionAnalysisResponseDto response = consumptionGoalService.getTopCategoryAndConsumptionAmount(
			user.getId());
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/month-report")
	public ApiResponse<MonthReportResponseDto> getMonthReport(
		@AuthUser UserDto.AuthUserDto user) {
		MonthReportResponseDto response = consumptionGoalService.getMonthReport(user.getId());
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/consumption-ment")
	public ApiResponse<String> getConsumptionMention(
		@AuthUser UserDto.AuthUserDto user) throws ExecutionException, InterruptedException {
		CompletableFuture<String> response = consumptionGoalService.getConsumptionMention(user.getId());
		return ApiResponse.onSuccess(response.get());
	}
}