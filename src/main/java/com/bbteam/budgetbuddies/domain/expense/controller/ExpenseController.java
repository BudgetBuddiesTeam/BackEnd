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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

	private final ExpenseService expenseService;

	@Operation(summary = "소비 내역 추가", description = "사용자가 소비 내역을 추가합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	})
	@PostMapping("/add")
	public ResponseEntity<ExpenseResponseDto> createExpense(
		@Parameter(description = "user_id, category_id, amount, description, expenseDate")
		@RequestBody ExpenseRequestDto expenseRequestDto) {
		ExpenseResponseDto response = expenseService.createExpense(expenseRequestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<MonthlyExpenseCompactResponseDto> findExpensesForMonth(
		Pageable pageable,
		@PathVariable @Param("userId") Long userId,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

		return ResponseEntity.ok(expenseService.getMonthlyExpense(pageable, userId, date));
	}
}
