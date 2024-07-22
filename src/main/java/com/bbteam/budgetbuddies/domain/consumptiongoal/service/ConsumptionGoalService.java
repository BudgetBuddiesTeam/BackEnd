package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumptionGoalService {
	private final ConsumptionGoalRepository consumptionGoalRepository;
	private final CategoryRepository categoryRepository;

	public ConsumptionGoalResponseListDto findUserConsumptionGoal(Long userId, LocalDate date) {
		LocalDate goalMonth = date.withDayOfMonth(1);
		Map<Long, ConsumptionGoalResponseDto> goalMap = initializeGoalMap(userId, goalMonth);

		updateGoalMapWithPreviousMonth(userId, goalMonth, goalMap);
		updateGoalMapWithCurrentMonth(userId, goalMonth, goalMap);

		return new ConsumptionGoalResponseListDto(new ArrayList<>(goalMap.values()));
	}

	private Map<Long, ConsumptionGoalResponseDto> initializeGoalMap(Long userId, LocalDate goalMonth) {
		return categoryRepository.findUserCategoryByUserId(userId)
			.stream()
			.collect(Collectors.toMap(
				Category::getId,
				category -> ConsumptionGoalResponseDto.initializeFromCategoryAndGoalMonth(category, goalMonth)
			));
	}

	private void updateGoalMapWithPreviousMonth(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		updateGoalMap(userId, goalMonth.minusMonths(1), goalMap);
	}

	private void updateGoalMapWithCurrentMonth(Long userId, LocalDate goalMonth,
		Map<Long, ConsumptionGoalResponseDto> goalMap) {
		updateGoalMap(userId, goalMonth, goalMap);
	}

	private void updateGoalMap(Long userId, LocalDate month, Map<Long, ConsumptionGoalResponseDto> goalMap) {
		consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(userId, month)
			.stream()
			.map(ConsumptionGoalResponseDto::of)
			.forEach(goal -> goalMap.put(goal.getCategoryId(), goal));
	}
}
