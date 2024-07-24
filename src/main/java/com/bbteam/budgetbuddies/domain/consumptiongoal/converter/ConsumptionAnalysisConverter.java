package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

public class ConsumptionAnalysisConverter {

	public static ConsumptionAnalysisResponseDTO fromEntity(ConsumptionGoal consumptionGoal, Long topAmount) {

		return ConsumptionAnalysisResponseDTO.builder()
			.goalCategory(consumptionGoal.getCategory().getName())
			.currentWeekConsumptionAmount(topAmount)
			.build();
	}
}
