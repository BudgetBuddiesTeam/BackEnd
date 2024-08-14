package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AvgConsumptionGoalDto {

	private Long categoryId;
	private Long averageAmount;

	public AvgConsumptionGoalDto(Long categoryId, Double averageAmount) {
		this.categoryId = categoryId;
		this.averageAmount = (averageAmount != null) ? averageAmount.longValue() : 0L;
	}
}