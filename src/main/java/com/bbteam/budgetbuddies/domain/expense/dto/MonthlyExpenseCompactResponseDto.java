package com.bbteam.budgetbuddies.domain.expense.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MonthlyExpenseCompactResponseDto {
	private LocalDate expenseMonth;
	private Long totalConsumptionAmount;

	private Map<String, List<CompactExpenseResponseDto>> expenses;
}
