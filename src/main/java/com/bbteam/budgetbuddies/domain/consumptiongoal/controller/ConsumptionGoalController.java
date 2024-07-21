package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption-goal")
public class ConsumptionGoalController {

	private final ConsumptionGoalService consumptionGoalService;

	@Operation(summary = "또래들이 가장 큰 계획을 세운 카테고리 조회 API", description = "특정 사용자의 소비 목표 카테고리별 소비 목표 금액을 조회하는 API 입니다.")
	@ApiResponses(value = {@ApiResponse(responseCode = "COMMON200", description = "OK, 성공")})
	@Parameters({@Parameter(name = "top", description = "가장 큰 목표를 세운 카테고리의 개수를 지정합니다. (기본값은 5입니다")})
	@GetMapping("/top-categories")
	public ResponseEntity<?> getTopGoalCategories(@RequestParam(name = "top", defaultValue = "5") int top,
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopGoalCategoryResponseDTO> topCategory = consumptionGoalService.getTopGoalCategories(top, userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(topCategory);
	}
}