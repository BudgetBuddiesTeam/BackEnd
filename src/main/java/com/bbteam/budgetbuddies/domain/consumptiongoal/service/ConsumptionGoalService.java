package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

	public ConsumptionGoalResponseListDto findUserConsumptionGoal(Long userId, LocalDate localDate) {
		final LocalDate goalMonth = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);

		Map<Long, ConsumptionGoalResponseDto> consumptionGoalResponseDtoMap = categoryRepository
			.findUserCategoryByUserId(userId)
			.stream()
			.map(c -> ConsumptionGoalResponseDto.initializeFromCategoryAndGoalMonth(c, goalMonth))
			.collect(Collectors.toMap(ConsumptionGoalResponseDto::getCategoryId, c -> c));

		List<ConsumptionGoalResponseDto> lastMonthConsumptionGoalList = consumptionGoalRepository
			.findConsumptionGoalByUserIdAndGoalMonth(userId, goalMonth.minusMonths(1))
			.stream().map(ConsumptionGoalResponseDto::of).toList();

		for (ConsumptionGoalResponseDto cgd : lastMonthConsumptionGoalList) {
			consumptionGoalResponseDtoMap.put(cgd.getCategoryId(), cgd);
		}

		List<ConsumptionGoalResponseDto> consumptionGoalList = consumptionGoalRepository
			.findConsumptionGoalByUserIdAndGoalMonth(userId, goalMonth)
			.stream().map(ConsumptionGoalResponseDto::of).toList();

		for (ConsumptionGoalResponseDto cgd : consumptionGoalList) {
			consumptionGoalResponseDtoMap.put(cgd.getCategoryId(), cgd);
		}

		return new ConsumptionGoalResponseListDto(consumptionGoalResponseDtoMap.values().stream().toList());
	}
}
