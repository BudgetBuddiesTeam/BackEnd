package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.util.List;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;

public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top);
}