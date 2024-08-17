package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption-goals")
public class ConsumptionGoalController implements ConsumptionGoalApi {

	private final ConsumptionGoalService consumptionGoalService;

	@Override
	@GetMapping("/categories/top-goals/top-4")
	public ApiResponse<List<TopGoalCategoryResponseDto>> getTopConsumptionGoalCategories(
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopGoalCategoryResponseDto> response = consumptionGoalService.getTopConsumptionGoalCategories(
			userId, peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping("/categories/top-goals")
	public ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionGoalCategories(
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<AllConsumptionCategoryResponseDto> response = consumptionGoalService.getAllConsumptionGoalCategories(
			userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@Override
	@GetMapping("/peer-info")
	public ApiResponse<PeerInfoResponseDto> getPeerInfo(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		PeerInfoResponseDto response = consumptionGoalService.getPeerInfo(userId, peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping("/{userId}")
	public ApiResponse<ConsumptionGoalResponseListDto> findUserConsumptionGoal(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable Long userId) {

		ConsumptionGoalResponseListDto response = consumptionGoalService.findUserConsumptionGoalList(userId, date);

		return ApiResponse.onSuccess(response);
	}

	@Override
	@PostMapping("/{userId}")
	public ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(@PathVariable Long userId,
		@RequestBody ConsumptionGoalListRequestDto consumptionGoalListRequestDto) {

		return ResponseEntity.ok()
			.body(consumptionGoalService.updateConsumptionGoals(userId, consumptionGoalListRequestDto));
	}

	@GetMapping("/categories/top-consumptions/top-3")
	public ApiResponse<List<TopCategoryConsumptionDto>> getTopConsumptionCategories(
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopCategoryConsumptionDto> response = consumptionGoalService.getTopConsumptionCategories(userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping("/categories/top-consumptions")
	public ApiResponse<List<AllConsumptionCategoryResponseDto>> getAllConsumptionCategories(
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<AllConsumptionCategoryResponseDto> response = consumptionGoalService.getAllConsumptionCategories(userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ApiResponse.onSuccess(response);
	}

	@GetMapping("/category/top-goals")
	public ApiResponse<ConsumptionAnalysisResponseDto> getTopCategoryAndConsumptionAmount(
		@RequestParam(name = "userId") Long userId) {
		ConsumptionAnalysisResponseDto response = consumptionGoalService.getTopCategoryAndConsumptionAmount(userId);
		return ApiResponse.onSuccess(response);
	}

}