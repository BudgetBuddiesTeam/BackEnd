package com.bbteam.budgetbuddies.domain.mainpage.converter;

import java.util.List;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;

public class MainPageConverter {
	public static MainPageResponseDto toMainPageResponseDto(
		List<DiscountResponseDto> discountResponseDtoList,
		List<SupportResponseDto> supportResponseDtoList,
		TopGoalCategoryResponseDto topGoalCategoryResponseDto,
		ConsumptionGoalResponseListDto consumptionGoalResponseListDto) {
		return MainPageResponseDto.builder()
			.discountResponseDtoList(discountResponseDtoList)
			.supportResponseDtoList(supportResponseDtoList)
			.topGoalCategoryResponseDto(topGoalCategoryResponseDto)
			.consumptionGoalResponseListDto(consumptionGoalResponseListDto)
			.build();
	}
}
