package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;

@Service
public interface ConsumptionGoalService {

	List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top, Long userId, int peerAgeStart, int peerAgeEnd,
		String peerGender);

	ConsumptionGoalResponseListDto findUserConsumptionGoal(Long userId, LocalDate date);

	PeerInfoResponseDTO getPeerInfo(Long userId, int peerAgeStart, int peerAgeEnd, String peerGender);

	ConsumptionAnalysisResponseDTO getTopCategoryAndConsumptionAmount(Long userId);
}