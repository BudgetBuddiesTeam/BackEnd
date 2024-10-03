package com.bbteam.budgetbuddies.domain.report.service;

public interface ReportService {
	/**
	 * 댓글에 대한 신고이력 여부 조회
	 * @param commentId
	 * @param userId
	 * @return 존재하면 true 존재하지 않으면 false
	 */
	boolean isExistReport(Long commentId, Long userId);
}
