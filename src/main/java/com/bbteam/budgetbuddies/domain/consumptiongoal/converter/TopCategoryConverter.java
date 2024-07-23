package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Component
public class TopCategoryConverter {

	public static TopGoalCategoryResponseDTO fromEntity(ConsumptionGoal consumptionGoal) {
		if (consumptionGoal == null || consumptionGoal.getCategory() == null) {
			return null;
		}

		return TopGoalCategoryResponseDTO.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.goalAmount(consumptionGoal.getGoalAmount())
			.build();
	}
}
