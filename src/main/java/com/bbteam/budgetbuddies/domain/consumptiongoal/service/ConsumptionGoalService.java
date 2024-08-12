package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

@Service
public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDTO> getTopGoalCategoriesLimit(int top, Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	ConsumptionGoalResponseListDto findUserConsumptionGoalList(Long userId, LocalDate date);

	PeerInfoResponseDTO getPeerInfo(Long userId, int peerAgeStart, int peerAgeEnd, String peerGender);

	ConsumptionGoalResponseListDto updateConsumptionGoals(Long userId,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto);

	ConsumptionAnalysisResponseDTO getTopCategoryAndConsumptionAmount(Long userId);

	void recalculateConsumptionAmount(Expense expense, ExpenseUpdateRequestDto request, User user);

	void updateConsumeAmount(Long userId, Long categoryId, Long amount);

	void decreaseConsumeAmount(Long userId, Long categoryId, Long amount, LocalDate expenseDate);

	List<TopConsumptionResponseDTO> getTopConsumptionsLimit(Long userId, int peerAgeS, int peerAgeE,
		String peerG);
}
