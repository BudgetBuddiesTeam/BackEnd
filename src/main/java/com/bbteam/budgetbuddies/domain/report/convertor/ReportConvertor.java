package com.bbteam.budgetbuddies.domain.report.convertor;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;
import com.bbteam.budgetbuddies.domain.report.entity.Report;
import com.bbteam.budgetbuddies.domain.user.entity.User;

@Component
public class ReportConvertor {
	public Report toEntity(ReportRequestDto request, User user, Comment comment) {
		return Report.builder().user(user).comment(comment).reason(request.getReason()).build();
	}

	public ReportResponseDto toReportResponse(Report report) {
		return ReportResponseDto.builder()
			.reportId(report.getId())
			.userId(report.getUser().getId())
			.commentId(report.getComment().getId())
			.build();
	}
}
