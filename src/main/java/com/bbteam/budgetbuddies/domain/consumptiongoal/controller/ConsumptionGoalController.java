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

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption-goal")
public class ConsumptionGoalController implements ConsumptionGoalApi {

	private final ConsumptionGoalService consumptionGoalService;

	@Override
	@GetMapping("/top-categories/top-goal/4")
	public ResponseEntity<?> getTopConsumptionGoalCategories(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopGoalCategoryResponseDto> topCategoriesList = consumptionGoalService.getTopConsumptionGoalCategories(
			userId, peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(topCategoriesList);
	}

	@GetMapping("/top-categories/top-goal")
	public ResponseEntity<?> getAllConsumptionGoalCategories(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopConsumptionResponseDto> response = consumptionGoalService.getAllConsumptionGoalCategories(userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/peer-info")
	public ResponseEntity<?> getPeerInfo(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		PeerInfoResponseDto response = consumptionGoalService.getPeerInfo(userId, peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ConsumptionGoalResponseListDto> findUserConsumptionGoal(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable Long userId) {

		ConsumptionGoalResponseListDto response = consumptionGoalService.findUserConsumptionGoalList(userId, date);

		return ResponseEntity.ok(response);
	}

	@Override
	@PostMapping("/{userId}")
	public ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(@PathVariable Long userId,
		@RequestBody ConsumptionGoalListRequestDto consumptionGoalListRequestDto) {

		return ResponseEntity.ok()
			.body(consumptionGoalService.updateConsumptionGoals(userId, consumptionGoalListRequestDto));
	}

	@GetMapping("/top-categories/top-consumption")
	public ResponseEntity<?> getAllConsumptionCategories(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopConsumptionResponseDto> response = consumptionGoalService.getAllConsumptionCategories(userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/top-categories/top-consumption/{userId}")
	public ResponseEntity<?> getTopCategoryAndConsumptionAmount(@PathVariable Long userId) {
		return ResponseEntity.ok(consumptionGoalService.getTopCategoryAndConsumptionAmount(userId));
	}
}