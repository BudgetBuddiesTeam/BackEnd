package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Component
public class ConsumptionGoalConverter {
	public ConsumptionGoalResponseDto toConsumptionGoalResponseDto(Category category) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(category.getName())
			.categoryId(category.getId())
			.goalAmount(0L)
			.consumeAmount(0L)
			.build();
	}

	public ConsumptionGoalResponseDto toConsumptionGoalResponseDto(ConsumptionGoal consumptionGoal) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.categoryId(consumptionGoal.getCategory().getId())
			.goalAmount(consumptionGoal.getGoalAmount())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.build();
	}
}
