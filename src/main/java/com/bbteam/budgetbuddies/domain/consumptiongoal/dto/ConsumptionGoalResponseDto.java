package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConsumptionGoalResponseDto {
	private String categoryName;
	private Long categoryId;
	private Long goalAmount;
	private Long consumeAmount;
	private Long remainingBalance;

	@Builder
	public ConsumptionGoalResponseDto(String categoryName, Long categoryId, Long goalAmount, Long consumeAmount) {
		this.categoryName = categoryName;
		this.categoryId = categoryId;
		this.goalAmount = goalAmount;
		this.consumeAmount = consumeAmount;
		this.remainingBalance = goalAmount - consumeAmount;
	}
}
