package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Component
public class TopCategoryConverter {

	public static TopGoalCategoryResponseDto fromEntity(ConsumptionGoal consumptionGoal) {
		if (consumptionGoal == null || consumptionGoal.getCategory() == null) {
			return null;
		}

		return TopGoalCategoryResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.goalAmount(consumptionGoal.getGoalAmount())
			.build();
	}
}
