package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryAvgConsumptionDTO {

	private Long categoryId;
	private Long averageConsumeAmount; // Change to Long

	public CategoryAvgConsumptionDTO(Long categoryId, Double averageConsumeAmount) {
		this.categoryId = categoryId;
		this.averageConsumeAmount = (averageConsumeAmount != null) ? averageConsumeAmount.longValue() : 0L;
	}
}