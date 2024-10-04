package com.bbteam.budgetbuddies.domain.report.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.report.convertor.ReportConvertor;
import com.bbteam.budgetbuddies.domain.report.dto.request.ReportRequestDto;
import com.bbteam.budgetbuddies.domain.report.dto.response.ReportResponseDto;
import com.bbteam.budgetbuddies.domain.report.entity.Report;
import com.bbteam.budgetbuddies.domain.report.repository.ReportRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

@DisplayName("Report 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
	@InjectMocks
	ReportServiceImpl reportService;

	@Mock
	ReportRepository reportRepository;
	@Mock
	UserRepository userRepository;
	@Mock
	CommentRepository commentRepository;
	@Spy
	ReportConvertor reportConvertor;

	User user;
	Comment comment;

	@BeforeEach
	void setUp() {
		user = Mockito.spy(User.builder().build());
		given(user.getId()).willReturn(-1L);

		comment = Mockito.spy(Comment.builder().build());
		given(comment.getId()).willReturn(-1L);
	}

	@Test
	void 댓글에_대한_신고_생성_성공() {
		// given
		ReportRequestDto request = Mockito.spy(new ReportRequestDto());
		given(request.getReason()).willReturn("TEST");

		// when
		when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
		when(commentRepository.findById(comment.getId())).thenReturn(Optional.ofNullable(comment));
		when(reportRepository.save(any(Report.class)))
			.thenAnswer(invocation -> {
				Report spyReport = spy((Report)invocation.getArgument(0));
				when(spyReport.getId()).thenReturn(-1L);
				return spyReport;
			});
		ReportResponseDto result = reportService.reportComment(request, user.getId(), comment.getId());

		// then
		assertThat(result.getReportId()).isEqualTo(-1L);
	}
}