package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllConsumptionCategoryResponseDto {

	private String categoryName;

	private Long avgAmount;

	private Long amountDifference;
}
