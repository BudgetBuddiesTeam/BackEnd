package com.bbteam.budgetbuddies.domain.consumptiongoal.converter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MonthReportResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.enums.Gender;

@Component
public class ConsumptionGoalConverter {
	public ConsumptionGoalResponseDto toConsumptionGoalResponseDto(ConsumptionGoal consumptionGoal) {
		return ConsumptionGoalResponseDto.builder()
			.categoryName(consumptionGoal.getCategory().getName())
			.categoryId(consumptionGoal.getCategory().getId())
			.goalAmount(consumptionGoal.getGoalAmount())
			.consumeAmount(consumptionGoal.getConsumeAmount())
			.build();
	}

	public ConsumptionGoalResponseListDto toConsumptionGoalResponseListDto(
		List<ConsumptionGoal> consumptionGoalList, LocalDate goalMonth) {

		List<ConsumptionGoalResponseDto> consumptionGoalResponseList = consumptionGoalList
			.stream()
			.map(this::toConsumptionGoalResponseDto)
			.sorted(Comparator.comparingLong(ConsumptionGoalResponseDto::getRemainingBalance).reversed())
			.toList();

		Long totalGoalAmount = sumTotalGoalAmount(consumptionGoalResponseList);
		Long totalConsumptionAmount = sumTotalConsumptionAmount(consumptionGoalResponseList);

		return ConsumptionGoalResponseListDto.builder()
			.goalMonth(goalMonth)
			.totalGoalAmount(totalGoalAmount)
			.totalConsumptionAmount(totalConsumptionAmount)
			.totalRemainingBalance(totalGoalAmount - totalConsumptionAmount)
			.consumptionGoalList(consumptionGoalResponseList)
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

	public ConsumptionAnalysisResponseDto toTopCategoryAndConsumptionAmount(String categoryName,
		Long topAmount) {

		return ConsumptionAnalysisResponseDto.builder()
			.goalCategory(categoryName)
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

	public MonthReportResponseDto toMonthReportResponseDto(String facialExpression, String comment) {

		return MonthReportResponseDto.builder()
			.facialExpression(facialExpression)
			.comment(comment)
			.build();
	}
}
