package com.bbteam.budgetbuddies.domain.expense.controller;

import java.time.LocalDate;

import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.service.ExpenseService;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.global.security.utils.AuthUser;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController implements ExpenseApi {
	private final ExpenseService expenseService;

	@Override
	@PostMapping("/add/{userId}")
	public ResponseEntity<DetailExpenseResponseDto> createExpense(
		@Parameter(description = "user_id") @PathVariable Long userId,
		@Parameter(description = "category_id, amount, description, expenseDate") @RequestBody ExpenseRequestDto expenseRequestDto) {
		DetailExpenseResponseDto response = expenseService.createExpense(userId, expenseRequestDto);
		return ResponseEntity.ok(response);
	}

	@Override
	@GetMapping()
	public ResponseEntity<MonthlyExpenseResponseDto> findExpensesForMonth(@AuthUser UserDto.AuthUserDto user,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		return ResponseEntity.ok(expenseService.getMonthlyExpense(user.getId(), date));
	}

	@Override
	@GetMapping("/{expenseId}")
	public ResponseEntity<DetailExpenseResponseDto> findExpense(@AuthUser UserDto.AuthUserDto user,
		@PathVariable @Param("expenseId") Long expenseId) {
		return ResponseEntity.ok(expenseService.findDetailExpenseResponse(user.getId(), expenseId));
	}

	@Override
	@PostMapping("/{userId}")
	public ResponseEntity<DetailExpenseResponseDto> updateExpense(@PathVariable @Param("userId") Long userId,
		@RequestBody ExpenseUpdateRequestDto request) {
		DetailExpenseResponseDto response = expenseService.updateExpense(userId, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete/{expenseId}")
	public ResponseEntity<String> deleteExpense(@Parameter(description = "expense_id") @PathVariable Long expenseId) {
		expenseService.deleteExpense(expenseId);
		return ResponseEntity.ok("Successfully deleted expense!");
	}
}
