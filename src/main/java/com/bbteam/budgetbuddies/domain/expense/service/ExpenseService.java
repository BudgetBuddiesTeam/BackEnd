package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.DetailExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseResponseDto;

public interface ExpenseService {
	DetailExpenseResponseDto createExpense(Long userId, ExpenseRequestDto expenseRequestDto);

	MonthlyExpenseResponseDto getMonthlyExpense(Long userId, LocalDate localDate);

	DetailExpenseResponseDto findDetailExpenseResponse(Long userId, Long expenseId);

	DetailExpenseResponseDto updateExpense(Long userId, ExpenseUpdateRequestDto request);

	void deleteExpense(Long expenseId);
}
