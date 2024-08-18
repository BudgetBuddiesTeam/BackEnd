package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;

public interface ExpenseService {
	ExpenseResponseDto createExpense(Long userId, ExpenseRequestDto expenseRequestDto);

	MonthlyExpenseCompactResponseDto getMonthlyExpense(Long userId, LocalDate localDate);

	ExpenseResponseDto findExpenseResponseFromUserIdAndExpenseId(Long userId, Long expenseId);

	ExpenseResponseDto updateExpense(Long userId, ExpenseUpdateRequestDto request);

	void deleteExpense(Long expenseId);
}
