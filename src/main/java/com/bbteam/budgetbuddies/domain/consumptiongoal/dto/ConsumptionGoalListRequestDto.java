package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ConsumptionGoalListRequestDto {
	List<ConsumptionGoalRequestDto> consumptionGoalList;
}
