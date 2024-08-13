package com.bbteam.budgetbuddies.domain.expense.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ExpenseUpdateRequestDto {
	private Long expenseId;
	private Long categoryId;
	private LocalDateTime expenseDate;
	private Long amount;
}
