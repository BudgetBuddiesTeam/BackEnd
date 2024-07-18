package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import com.bbteam.budgetbuddies.domain.category.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopGoalCategoryResponseDTO {

	private Category category;

	private Long goalAmount;

}