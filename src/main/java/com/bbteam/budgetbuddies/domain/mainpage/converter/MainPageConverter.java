package com.bbteam.budgetbuddies.domain.mainpage.converter;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;

import java.util.List;

public class MainPageConverter {
    public static MainPageResponseDto toMainPageResponseDto(
            List<DiscountResponseDto> discountResponseDtoList,
            List<SupportResponseDto> supportResponseDtoList,
            TopGoalCategoryResponseDTO topGoalCategoryResponseDTO,
            ConsumptionGoalResponseListDto consumptionGoalResponseListDto) {
        return MainPageResponseDto.builder()
                .discountResponseDtoList(discountResponseDtoList)
                .supportResponseDtoList(supportResponseDtoList)
                .topGoalCategoryResponseDTO(topGoalCategoryResponseDTO)
                .consumptionGoalResponseListDto(consumptionGoalResponseListDto)
                .build();
    }
}
