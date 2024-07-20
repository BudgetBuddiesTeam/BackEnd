package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.TopCategoryConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumptionGoalServiceImpl implements ConsumptionGoalService {

	private final ConsumptionGoalRepository consumptionGoalRepository;
	private final TopCategoryConverter topCategoryConverter;

	@Override
	@Transactional(readOnly = true)
	public List<TopGoalCategoryResponseDTO> getTopGoalCategories(int top) {
		List<ConsumptionGoal> topGoals = consumptionGoalRepository.findTopCategoriesAndGoalAmount();
		return topGoals.stream()
			.limit(top) // 여기서 top 개수만큼 제한
			.map(topCategoryConverter::fromEntity)
			.collect(Collectors.toList());
	}
}
