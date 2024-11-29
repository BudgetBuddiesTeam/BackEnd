package com.bbteam.budgetbuddies.domain.report.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class ReportResponseDto {
	private Long reportId;
	private Long userId;
	private Long commentId;
}
