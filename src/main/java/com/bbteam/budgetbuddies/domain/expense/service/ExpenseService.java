package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;

public interface ExpenseService {
	ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto);

	MonthlyExpenseCompactResponseDto getMonthlyExpense(Pageable pageable, Long userId, LocalDate localDate);
}
