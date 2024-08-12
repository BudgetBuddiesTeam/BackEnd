package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

public class ConsumptionAnalysisConverter {

	public static ConsumptionAnalysisResponseDto fromEntity(ConsumptionGoal consumptionGoal, Long topAmount) {

		return ConsumptionAnalysisResponseDto.builder()
			.goalCategory(consumptionGoal.getCategory().getName())
			.currentWeekConsumptionAmount(topAmount)
			.build();
	}
}
