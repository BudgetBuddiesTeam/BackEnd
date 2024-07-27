package com.bbteam.budgetbuddies.domain.mainpage.dto;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponseDto {

    private TopGoalCategoryResponseDTO topGoalCategoryResponseDTO;
    private ConsumptionGoalResponseListDto consumptionGoalResponseListDto;
    private List<DiscountResponseDto> discountResponseDtoList;
    private List<SupportResponseDto> supportResponseDtoList;
    // 기존 DTO들 활용

}
