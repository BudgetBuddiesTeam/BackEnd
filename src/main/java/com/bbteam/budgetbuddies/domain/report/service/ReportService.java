package com.bbteam.budgetbuddies.domain.report.service;

import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;

public interface ReportService {
	/**
	 * 댓글에 대한 신고이력 여부 조회
	 * @param commentId
	 * @param userId
	 * @return 존재하면 true 존재하지 않으면 false
	 */
	boolean isExistReport(Long commentId, Long userId);

	/**
	 * 댓글에 대한 신고 생성
	 * @param request
	 * @param commentId
	 * @param userId
	 * @return reportID를 포함한 신고정보
	 */
	ReportResponseDto reportComment(ReportRequestDto request, Long commentId, Long userId);
}
