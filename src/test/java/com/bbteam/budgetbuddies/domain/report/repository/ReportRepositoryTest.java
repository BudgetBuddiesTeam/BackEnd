package com.bbteam.budgetbuddies.domain.report.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.report.entity.Report;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

@DisplayName("Report 레포지토리 테스트의")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReportRepositoryTest {
	@Autowired
	ReportRepository reportRepository;

	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;

	@Test
	void userId와_commentId를_통해서_Report_존재_여부_조회() {
		User user = userRepository.save(User.builder()
			.email("testUser@example.com")
			.mobileCarrier("TEST")
			.age(24)
			.name("Test User")
			.gender(Gender.MALE)
			.phoneNumber("010-1234-5678")
			.build());

		Comment comment = commentRepository.save(
			Comment.builder().user(user).content("test2").anonymousNumber(1212).build());

		reportRepository.save(Report.builder().user(user).comment(comment).reason("test1").build());

		assertTrue(reportRepository.existsByUser_IdAndComment_Id(user.getId(), comment.getId()));
	}
}