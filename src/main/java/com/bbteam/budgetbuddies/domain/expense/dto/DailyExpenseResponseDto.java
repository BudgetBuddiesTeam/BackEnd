package com.bbteam.budgetbuddies.domain.expense.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class DailyExpenseResponseDto {
	private Integer daysOfMonth;
	private String daysOfTheWeek;
	private List<CompactExpenseResponseDto> expenses;
}
