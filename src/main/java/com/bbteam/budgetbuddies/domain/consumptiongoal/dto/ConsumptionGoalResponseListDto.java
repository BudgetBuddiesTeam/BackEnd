package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsumptionGoalResponseListDto {
	private List<ConsumptionGoalResponseDto> consumptionGoalList;

	public ConsumptionGoalResponseListDto(List<ConsumptionGoalResponseDto> consumptionGoalResponseDtoList) {
		this.consumptionGoalList = consumptionGoalResponseDtoList;
	}
}
