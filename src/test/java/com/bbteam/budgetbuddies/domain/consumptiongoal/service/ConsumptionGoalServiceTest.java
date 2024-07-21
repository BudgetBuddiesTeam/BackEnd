package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;

// TODO 꺠끗하게 작성하기;
@DisplayName("ConsumptionGoal 테스트의 ")
@ExtendWith(MockitoExtension.class)
class ConsumptionGoalServiceTest {
	@InjectMocks
	private ConsumptionGoalService consumptionGoalService;

	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private ConsumptionGoalRepository consumptionGoalRepository;

	@Test
	@DisplayName("findUserConsumptionGoal 성공")
	void findUserConsumptionGoal_success() {
		LocalDate goalMonth = LocalDate.of(2024, 07, 01);

		User user = Mockito.spy(User.builder()
			.email("email")
			.age(24)
			.name("name")
			.phoneNumber("010-1234-5678")
			.build());
		given(user.getId()).willReturn(-1L);

		Category defaultCategory = Mockito.spy(Category.builder()
			.name("디폴트 카테고리")
			.user(null)
			.isDefault(true)
			.build());
		given(defaultCategory.getId()).willReturn(-1L);

		Category userCategory = Mockito.spy(Category.builder()
			.name("유저 카테고리")
			.user(user)
			.isDefault(false)
			.build());
		given(userCategory.getId()).willReturn(-2L);

		Category userCategory2 = Mockito.spy(Category.builder()
			.name("유저 카테고리2")
			.user(user)
			.isDefault(false)
			.build());
		given(userCategory2.getId()).willReturn(-3L);

		Category userCategory3 = Mockito.spy(Category.builder()
			.name("유저 카테고리3")
			.user(user)
			.isDefault(false)
			.build());
		given(userCategory3.getId()).willReturn(-4L);

		given(categoryRepository.findUserCategoryByUserId(user.getId()))
			.willReturn(List.of(defaultCategory, userCategory, userCategory2, userCategory3));

		// default
		ConsumptionGoal consumptionGoal1 = ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonth)
			.build();

		// custom
		ConsumptionGoal consumptionGoal2 = ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(user)
			.category(userCategory2)
			.goalMonth(goalMonth)
			.build();

		// 1달 전
		ConsumptionGoal consumptionGoal3 = ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(user)
			.category(userCategory2)
			.goalMonth(goalMonth.withMonth(1))
			.build();

		given(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(), goalMonth)).willReturn(
			List.of(consumptionGoal1, consumptionGoal2));

		given(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(), goalMonth.minusMonths(1)))
			.willReturn(List.of(consumptionGoal3));

		List<ConsumptionGoalResponseDto> expected = List.of(
			ConsumptionGoalResponseDto.of(consumptionGoal1), // default
			ConsumptionGoalResponseDto.initializeFromCategoryAndGoalMonth(userCategory, goalMonth), // 1
			ConsumptionGoalResponseDto.of(consumptionGoal2), // 2
			ConsumptionGoalResponseDto.initializeFromCategoryAndGoalMonth(userCategory3, goalMonth)); // 3

		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoal(user.getId(),
			goalMonth.plusDays(2));

		assertThat(result.getConsumptionGoalResponseDtoList()).usingRecursiveComparison().isEqualTo(expected);
	}
}