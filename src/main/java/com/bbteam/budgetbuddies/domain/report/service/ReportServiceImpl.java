package com.bbteam.budgetbuddies.domain.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.report.convertor.ReportConvertor;
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
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("not found user"));
		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("not found comment"));

		return reportRepository.existsByUserAndComment(user, comment);
	}
}
