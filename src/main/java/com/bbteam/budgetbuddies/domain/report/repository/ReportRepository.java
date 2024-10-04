package com.bbteam.budgetbuddies.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.report.entity.Report;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
	boolean existsByUser_IdAndComment_Id(Long userId, Long CommentId);
}
