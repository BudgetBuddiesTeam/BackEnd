package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;

@Service
public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top, Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);
}