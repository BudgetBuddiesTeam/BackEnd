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
public class MonthlyExpenseCompactResponseDto {
	private LocalDate expenseMonth;
	private int currentPage;
	private boolean hasNext;
	private List<CompactExpenseResponseDto> expenseList;
}
