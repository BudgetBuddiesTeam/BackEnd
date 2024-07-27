package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ConsumptionGoalRequestDto {
	private Long categoryId;
	private Long goalAmount;
}
