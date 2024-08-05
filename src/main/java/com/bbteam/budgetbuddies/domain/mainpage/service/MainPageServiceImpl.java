package com.bbteam.budgetbuddies.domain.mainpage.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopGoalCategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.service.DiscountInfoService;
import com.bbteam.budgetbuddies.domain.mainpage.converter.MainPageConverter;
import com.bbteam.budgetbuddies.domain.mainpage.dto.MainPageResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.service.SupportInfoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
	private final DiscountInfoService discountInfoService;
	private final SupportInfoService supportInfoService;
	private final ConsumptionGoalService consumptionGoalService;

	@Override
	public MainPageResponseDto getMainPage(Long userId) {
		LocalDate now = LocalDate.now();

		List<DiscountResponseDto> discountResponseDtoList = discountInfoService.getDiscountsByYearAndMonth(
				now.getYear(), now.getMonthValue(), 0, 2)
			.getContent();
		List<SupportResponseDto> supportResponseDtoList = supportInfoService.getSupportsByYearAndMonth(now.getYear(),
				now.getMonthValue(), 0, 2)
			.getContent();

		List<TopGoalCategoryResponseDTO> topGoalCategoryResponseDTOList = consumptionGoalService.getTopGoalCategoriesLimit(
			1, userId, 0, 0, "NONE");
		if (topGoalCategoryResponseDTOList.size() == 0) {
			throw new NoSuchElementException("Category xx");
		}
		TopGoalCategoryResponseDTO topGoalCategoryResponseDTO = topGoalCategoryResponseDTOList.get(0);

		ConsumptionGoalResponseListDto userConsumptionGoal = consumptionGoalService.findUserConsumptionGoal(userId,
			now);

		return MainPageConverter.toMainPageResponseDto(discountResponseDtoList, supportResponseDtoList,
			topGoalCategoryResponseDTO, userConsumptionGoal);
	}
}
