package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeAmountAndGoalAmountDto {

	private Long categoryId;
	private Long consumeAmount;
	private Long goalAmount;
}
