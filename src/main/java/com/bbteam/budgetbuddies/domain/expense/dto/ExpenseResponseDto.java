package com.bbteam.budgetbuddies.domain.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDto {
    private Long expenseId;
    private Long userId;
    private Long categoryId;
    private String categoryName;
    private Long amount;
    private String description;
    private LocalDateTime expenseDate;
}
