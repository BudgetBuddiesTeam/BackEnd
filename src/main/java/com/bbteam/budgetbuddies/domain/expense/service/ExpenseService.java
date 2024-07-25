package com.bbteam.budgetbuddies.domain.expense.service;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;

import java.util.List;

public interface ExpenseService {
    ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto);
}
