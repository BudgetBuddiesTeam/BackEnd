package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

@Component
public class TopConsumptionConverter {
	public static TopConsumptionResponseDTO fromEntity(ConsumptionGoal consumptionGoal) {

		return TopConsumptionResponseDTO.builder()
			.category(consumptionGoal.getCategory().getName())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.build();
	}
}
