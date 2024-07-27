package com.bbteam.budgetbuddies.domain.expense.controller;

import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExpenseApi {
	@Operation(summary = "소비 내역 추가", description = "사용자가 소비 내역을 추가합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
			@ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	})
	ResponseEntity<ExpenseResponseDto> createExpense(
			@Parameter(description = "user_id, category_id, amount, description, expenseDate") @RequestBody ExpenseRequestDto expenseRequestDto);

	@Operation(summary = "월별 소비 조회", description = "무한 스크롤을 통한 조회로 예상하여 Slice를 통해서 조회")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
			@ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
			@ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	})
	ResponseEntity<MonthlyExpenseCompactResponseDto> findExpensesForMonth(Pageable pageable, Long userId, LocalDate date);
}
