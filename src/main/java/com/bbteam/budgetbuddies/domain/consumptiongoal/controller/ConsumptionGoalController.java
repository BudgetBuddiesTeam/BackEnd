package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ConsumptionGoalController {

	private final ConsumptionGoalService consumptionGoalService;

	@GetMapping("/consumption-goal/top-categories")
	public ResponseEntity<?> getTopGoalCategories(@RequestParam(name = "top", defaultValue = "5") int top) {
		List<TopGoalCategoryResponseDTO> topCategory = consumptionGoalService.getTopGoalCategories(top);
		return ResponseEntity.ok(topCategory);
	}
}