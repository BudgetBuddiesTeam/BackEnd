package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ConsumptionGoalResponseListDto {
	private LocalDate goalMonth;
	private Long totalGoalAmount;
	private Long totalConsumptionAmount;
	private Long totalRemainingBalance;
	private List<ConsumptionGoalResponseDto> consumptionGoalList;
}
