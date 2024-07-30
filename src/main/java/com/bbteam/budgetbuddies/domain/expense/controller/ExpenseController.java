package com.bbteam.budgetbuddies.domain.expense.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;
import com.bbteam.budgetbuddies.domain.expense.service.ExpenseService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController implements ExpenseApi {
	private final ExpenseService expenseService;

	@Override
	@PostMapping("/add")
	public ResponseEntity<ExpenseResponseDto> createExpense(
		@Parameter(description = "user_id, category_id, amount, description, expenseDate") @RequestBody ExpenseRequestDto expenseRequestDto) {
		ExpenseResponseDto response = expenseService.createExpense(expenseRequestDto);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping("/{userId}")
	public ResponseEntity<MonthlyExpenseCompactResponseDto> findExpensesForMonth(Pageable pageable,
		@PathVariable @Param("userId") Long userId,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		return ResponseEntity.ok(expenseService.getMonthlyExpense(pageable, userId, date));
	}

	@Override
	@GetMapping("/{userId}/{expenseId}")
	public ResponseEntity<ExpenseResponseDto> findExpense(@PathVariable Long userId, @PathVariable Long expenseId) {

		return ResponseEntity.ok(expenseService.findExpenseResponseFromUserIdAndExpenseId(userId, expenseId));
	}
}
