package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopConsumptionResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;

@Service
public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDTO> getTopGoalCategoriesLimit(int top, Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	Page<TopGoalCategoryResponseDTO> getTopGoalCategories(Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender, Pageable pageable);

	ConsumptionGoalResponseListDto findUserConsumptionGoal(Long userId, LocalDate date);

	PeerInfoResponseDTO getPeerInfo(Long userId, int peerAgeStart, int peerAgeEnd, String peerGender);

	ConsumptionGoalResponseListDto updateConsumptionGoals(Long userId,
		ConsumptionGoalListRequestDto consumptionGoalListRequestDto);

	ConsumptionAnalysisResponseDTO getTopCategoryAndConsumptionAmount(Long userId);

	void updateConsumeAmount(Long userId, Long categoryId, Long amount);

	List<TopConsumptionResponseDTO> getTopConsumption(int top, Long userId, int peerAgeS, int peerAgeE,
		String peerG);
}