package com.bbteam.budgetbuddies.domain.expense.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpenseUpdateRequestDto {
	private Long expenseId;
	private Long categoryId;
	private LocalDateTime expenseDate;
	private Long amount;
}
