package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvgConsumptionGoalDTO {

	private Long categoryId;
	private Long averageAmount;

	public AvgConsumptionGoalDTO(Long categoryId, Double averageAmount) {
		this.categoryId = categoryId;
		this.averageAmount = (averageAmount != null) ? averageAmount.longValue() : 0L;
	}
}