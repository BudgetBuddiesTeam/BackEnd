package com.bbteam.budgetbuddies.domain.mainpage.dto;

import java.util.List;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainPageResponseDto {

	private ConsumptionAnalysisResponseDto consumptionAnalysisResponseDto;
	private ConsumptionGoalResponseListDto consumptionGoalResponseListDto;
	private List<DiscountResponseDto> discountResponseDtoList;
	private List<SupportResponseDto> supportResponseDtoList;
	// 기존 DTO들 활용

}
