package com.bbteam.budgetbuddies.domain.report.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;
import com.bbteam.budgetbuddies.domain.report.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/{commentId}/report")
@RequiredArgsConstructor
public class ReportController implements ReportApi {
	private final ReportService reportService;

	@GetMapping("/{userId}")
	public ApiResponse<String> isExistReport(@PathVariable @Param("commentId") Long commentId,
		@PathVariable @Param("userId") Long userId) {
		if (reportService.isExistReport(commentId, userId)) {
			return ApiResponse.onSuccess("신고 이력이 존재합니다.");
		}
		return ApiResponse.onFailure(HttpStatus.NOT_FOUND.toString(), HttpStatus.NOT_FOUND.getReasonPhrase(),
			"신고 이력이 존재하지 않습니다.");
	}

	@PostMapping("/{userId}")
	public ApiResponse<ReportResponseDto> reportComment(@Valid @RequestBody ReportRequestDto request,
		@PathVariable @Param("commentId") Long commentId, @PathVariable @Param("userId") Long userId) {

		ReportResponseDto response = reportService.reportComment(request, commentId, userId);
		return ApiResponse.onSuccess(response);
	}
}
