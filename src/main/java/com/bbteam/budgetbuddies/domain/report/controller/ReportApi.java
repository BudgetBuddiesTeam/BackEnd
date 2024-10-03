package com.bbteam.budgetbuddies.domain.report.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface ReportApi {
	@Operation(summary = "[User] 댓글에대한 신고여부 조회", description = "존재하지 않을 경우에만 신고 뷰로 이동")
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON404", description = "NOTFOUND, 실패")
	})
	@Parameters({
		@Parameter(name = "userId", description = "로그인 한 유저 아이디"),
		@Parameter(name = "peerAgeStart", description = "또래나이 시작 범위")
	})
	ApiResponse<String> isExistReport(Long commentId, Long userId);
}