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
public class CompactExpenseResponseDto {
	private Long expenseId;
	private String description;
	private Long amount;
	private LocalDateTime expenseDate;
}
