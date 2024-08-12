package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.enums.Gender;

@Component
public class ConsumptionGoalConverter {
	public ConsumptionGoalResponseDto toConsumptionGoalResponseDto(Category category) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(category.getName())
			.categoryId(category.getId())
			.goalAmount(0L)
			.consumeAmount(0L)
			.build();
	}

	public ConsumptionGoalResponseDto toConsumptionGoalResponseDto(ConsumptionGoal consumptionGoal) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.categoryId(consumptionGoal.getCategory().getId())
			.goalAmount(consumptionGoal.getGoalAmount())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.build();
	}

	public ConsumptionGoalResponseListDto toConsumptionGoalResponseListDto(
		List<ConsumptionGoalResponseDto> consumptionGoalList, LocalDate goalMonth) {
		return ConsumptionGoalResponseListDto.builder()
			.goalMonth(goalMonth)
			.totalGoalAmount(sumTotalGoalAmount(consumptionGoalList))
			.totalConsumptionAmount(sumTotalConsumptionAmount(consumptionGoalList))
			.consumptionGoalList(consumptionGoalList)
			.build();

	}

	private Long sumTotalConsumptionAmount(List<ConsumptionGoalResponseDto> consumptionGoalList) {
		return consumptionGoalList.stream().reduce(0L, (sum, c2) -> sum + c2.getConsumeAmount(), Long::sum);
	}

	private Long sumTotalGoalAmount(List<ConsumptionGoalResponseDto> consumptionGoalList) {
		return consumptionGoalList.stream().reduce(0L, (sum, c2) -> sum + c2.getGoalAmount(), Long::sum);
	}

	public UserConsumptionGoalResponse toUserConsumptionGoalResponse(ConsumptionGoal consumptionGoal) {
		return UserConsumptionGoalResponse.builder()
			.categoryId(consumptionGoal.getCategory().getId())
			.goalMonth(consumptionGoal.getGoalMonth())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.goalAmount(consumptionGoal.getGoalAmount())
			.build();
	}

	public ConsumptionAnalysisResponseDto toTopCategoryAndConsumptionAmount(ConsumptionGoal consumptionGoal,
		Long topAmount) {

		return ConsumptionAnalysisResponseDto.builder()
			.goalCategory(consumptionGoal.getCategory().getName())
			.currentWeekConsumptionAmount(topAmount)
			.build();
	}

	public PeerInfoResponseDto toPeerInfo(int peerAgeStart, int peerAgeEnd, Gender peerGender) {

		return PeerInfoResponseDto.builder()
			.peerAgeStart(peerAgeStart)
			.peerAgeEnd(peerAgeEnd)
			.peerGender(peerGender.name())
			.build();
	}

	public TopGoalCategoryResponseDto toTopGoalCategories(ConsumptionGoal consumptionGoal) {
		if (consumptionGoal == null || consumptionGoal.getCategory() == null) {
			return null;
		}

		return TopGoalCategoryResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.goalAmount(consumptionGoal.getGoalAmount())
			.build();
	}
}
