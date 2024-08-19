package com.bbteam.budgetbuddies.domain.expense.dto;

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
public class MonthlyExpenseResponseDto {
	private LocalDate expenseMonth;
	private Long totalConsumptionAmount;

	private List<DailyExpenseResponseDto> dailyExpenses;
}
