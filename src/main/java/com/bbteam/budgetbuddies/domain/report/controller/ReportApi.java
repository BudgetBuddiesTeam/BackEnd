package com.bbteam.budgetbuddies.domain.report.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

public interface ReportApi {
	@Operation(summary = "[User] 댓글에대한 신고여부 조회", description = "존재하지 않을 경우에만 신고 뷰로 이동")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON404", description = "NOTFOUND, 실패")
	})
	@Parameters({
		@Parameter(name = "commentId", description = "신고할 댓글 아이디"),
		@Parameter(name = "userId", description = "로그인 한 유저 아이디")
	})
	ApiResponse<String> isExistReport(Long commentId, Long userId);

	@Operation(summary = "[User] 댓글에대한 신고")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
	})
	@Parameters({
		@Parameter(name = "commentId", description = "신고할 댓글 아이디"),
		@Parameter(name = "userId", description = "로그인 한 유저 아이디")
	})
	ApiResponse<ReportResponseDto> reportComment(@Valid @RequestBody ReportRequestDto request,
		@PathVariable @Param("commentId") Long commentId, @PathVariable @Param("userId") Long userId);
}