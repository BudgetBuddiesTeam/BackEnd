package com.bbteam.budgetbuddies.domain.consumptiongoal.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumption-goal")
public class ConsumptionGoalController implements ConsumptionGoalApi {

	private final ConsumptionGoalService consumptionGoalService;

	@GetMapping("/top-categories/top-goal/{top}")
	public ResponseEntity<?> getTopGoalCategoriesList(@PathVariable(name = "top") int top,
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopGoalCategoryResponseDTO> topCategoriesList = consumptionGoalService.getTopGoalCategoriesLimit(top,
			userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(topCategoriesList);
	}

	@GetMapping("/top-categories/top-goal")
	public ResponseEntity<?> getTopGoalCategoriesPage(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender, Pageable pageable) {
		Page<TopGoalCategoryResponseDTO> topCategoriesPage = consumptionGoalService.getTopGoalCategories(userId,
			peerAgeStart, peerAgeEnd, peerGender, pageable);
		return ResponseEntity.ok(topCategoriesPage.getContent());
	}

	@GetMapping("/top-category/top-goal")
	public ResponseEntity<?> getTopGoalCategory(@RequestParam(name = "userId") Long userId) {
		ConsumptionAnalysisResponseDTO response = consumptionGoalService.getTopCategoryAndConsumptionAmount(userId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/peer-info")
	public ResponseEntity<?> getPeerInfo(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		PeerInfoResponseDTO response = consumptionGoalService.getPeerInfo(userId, peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<ConsumptionGoalResponseListDto> findUserConsumptionGoal(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @PathVariable Long userId) {

		ConsumptionGoalResponseListDto response = consumptionGoalService.findUserConsumptionGoal(userId, date);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/{userId}")
	public ResponseEntity<ConsumptionGoalResponseListDto> updateOrElseGenerateConsumptionGoal(@PathVariable Long userId,
		@RequestBody ConsumptionGoalListRequestDto consumptionGoalListRequestDto) {

		return ResponseEntity.ok()
			.body(consumptionGoalService.updateConsumptionGoals(userId, consumptionGoalListRequestDto));
	}

	@GetMapping("/top-categories/top-consumption/{top}")
	public ResponseEntity<?> getConsumptionGoalList(@PathVariable(name = "top") int top,
		@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender) {
		List<TopConsumptionResponseDTO> response = consumptionGoalService.getTopConsumptionsLimit(top, userId,
			peerAgeStart, peerAgeEnd, peerGender);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/top-categories/top-consumption")
	public ResponseEntity<?> getConsumptionGoalPage(@RequestParam(name = "userId") Long userId,
		@RequestParam(name = "peerAgeStart", defaultValue = "0") int peerAgeStart,
		@RequestParam(name = "peerAgeEnd", defaultValue = "0") int peerAgeEnd,
		@RequestParam(name = "peerGender", defaultValue = "none") String peerGender, Pageable pageable) {
		Page<TopConsumptionResponseDTO> response = consumptionGoalService.getTopConsumptions(userId,
			peerAgeStart, peerAgeEnd, peerGender, pageable);
		return ResponseEntity.ok(response.getContent());
	}
}