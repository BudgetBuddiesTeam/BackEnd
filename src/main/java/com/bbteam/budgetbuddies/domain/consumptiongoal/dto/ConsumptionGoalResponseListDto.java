package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsumptionGoalResponseListDto {
	private LocalDate goalMonth;
	private List<ConsumptionGoalResponseDto> consumptionGoalList;

	public ConsumptionGoalResponseListDto(LocalDate goalMonth, List<ConsumptionGoalResponseDto> consumptionGoalList) {
		this.goalMonth = goalMonth;
		this.consumptionGoalList = consumptionGoalList;
	}
}
