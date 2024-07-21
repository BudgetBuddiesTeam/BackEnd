package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import java.time.LocalDate;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ConsumptionGoalResponseDto {
	private String categoryName;
	private Long categoryId;
	private Long goalAmount;
	private Long consumeAmount;
	private LocalDate goalMonth;

	@Builder
	public ConsumptionGoalResponseDto(String categoryName, Long categoryId, Long goalAmount, Long consumeAmount,
		LocalDate goalMonth) {
		this.categoryName = categoryName;
		this.categoryId = categoryId;
		this.goalAmount = goalAmount;
		this.consumeAmount = consumeAmount;
		this.goalMonth = goalMonth;
	}

	public static ConsumptionGoalResponseDto initializeFromCategoryAndGoalMonth(Category category,
		LocalDate goalMonth) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(category.getName())
			.categoryId(category.getId())
			.goalAmount(0L)
			.consumeAmount(0L)
			.goalMonth(goalMonth)
			.build();
	}

	public static ConsumptionGoalResponseDto of(ConsumptionGoal consumptionGoal) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.categoryId(consumptionGoal.getCategory().getId())
			.goalAmount(consumptionGoal.getGoalAmount())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.goalMonth(consumptionGoal.getGoalMonth())
			.build();
	}
}
