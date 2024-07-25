package com.bbteam.budgetbuddies.domain.expense.service;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;

public interface ExpenseService {
    ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto);
}
