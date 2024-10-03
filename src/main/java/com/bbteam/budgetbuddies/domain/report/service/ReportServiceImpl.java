package com.bbteam.budgetbuddies.domain.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.report.convertor.ReportConvertor;
import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;
import com.bbteam.budgetbuddies.domain.report.entity.Report;
import com.bbteam.budgetbuddies.domain.report.repository.ReportRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
	private final ReportRepository reportRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	private final ReportConvertor reportConvertor;

	@Override
	@Transactional(readOnly = true)
	public boolean isExistReport(Long commentId, Long userId) {
		return reportRepository.existsByUser_IdAndComment_Id(userId, commentId);
	}

	@Override
	@Transactional
	public ReportResponseDto reportComment(ReportRequestDto request, Long commentId, Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("not found comment"));

		Report report = reportConvertor.toEntity(request, user, comment);
		Report savedReport = reportRepository.save(report);

		return reportConvertor.toReportResponse(savedReport);
	}
}
