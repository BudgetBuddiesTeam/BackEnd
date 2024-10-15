package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AllConsumptionCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MonthReportResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

@Service
public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDto> getTopConsumptionGoalCategories(Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	List<AllConsumptionCategoryResponseDto> getAllConsumptionGoalCategories(Long userId, int peerAgeS, int peerAgeE,
		String peerG);

	ConsumptionGoalResponseListDto findUserConsumptionGoalList(Long userId, LocalDate date);

	PeerInfoResponseDto getPeerInfo(Long userId, int peerAgeStart, int peerAgeEnd, String peerGender);

	ConsumptionGoalResponseListDto updateConsumptionGoals(Long userId,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto);

	ConsumptionAnalysisResponseDto getTopCategoryAndConsumptionAmount(Long userId);

	void recalculateConsumptionAmount(Expense expense, ExpenseUpdateRequestDto request, User user);

	void updateConsumeAmount(Long userId, Long categoryId, Long amount);

	void decreaseConsumeAmount(Long userId, Long categoryId, Long amount, LocalDate expenseDate);

	List<TopCategoryConsumptionDto> getTopConsumptionCategories(Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	List<AllConsumptionCategoryResponseDto> getAllConsumptionCategories(Long userId, int peerAgeS, int peerAgeE,
		String peerG);

	void updateOrCreateDeletedConsumptionGoal(Long userId, Long categoryId, LocalDate goalMonth, Long amount);

	MonthReportResponseDto getMonthReport(Long userId);

	CompletableFuture<String> getConsumptionMention(Long userId);
}
