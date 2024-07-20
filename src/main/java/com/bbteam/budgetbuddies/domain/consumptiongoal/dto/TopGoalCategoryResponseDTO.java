package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopGoalCategoryResponseDTO {

	private String categoryName;

	private Long goalAmount;

}