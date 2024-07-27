package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDTO;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

@DisplayName("ConsumptionGoalImpl 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
class ConsumptionGoalServiceTest {
	private final LocalDate GOAL_MONTH = LocalDate.of(2024, 07, 01);
	private User user;
	private LocalDate goalMonthRandomDay;

	@InjectMocks
	private ConsumptionGoalServiceImpl consumptionGoalService;
	@Mock
	private ConsumptionGoalRepository consumptionGoalRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private UserRepository userRepository;
	@Spy
	private ConsumptionGoalConverter consumptionGoalConverter;

	@BeforeEach
	void setUp() {
		Random random = new Random();
		int randomDay = random.nextInt(30) + 1;
		goalMonthRandomDay = LocalDate.of(GOAL_MONTH.getYear(), GOAL_MONTH.getMonth(), randomDay);

		user = Mockito.spy(User.builder().email("email").age(24).name("name").gender(Gender.MALE).phoneNumber("010-1234-5678").build());
		given(user.getId()).willReturn(-1L);
		given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
	}

	@Test
	@DisplayName("findUserConsumptionGoal : 생성된 ConsumptionGoal이 없고 카테고리만 있는 경우 목표 금액, 소비 금액 0으로 초기화")
	void findUserConsumptionGoal_onlyCategory() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);

		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		List<Category> categoryList = List.of(defaultCategory, userCategory);

		given(categoryRepository.findUserCategoryByUserId(user.getId())).willReturn(categoryList);

		List<ConsumptionGoalResponseDto> expected = categoryList.stream()
			.map(category -> consumptionGoalConverter.toConsumptionGoalResponseDto(category))
			.toList();

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoal(user.getId(),
			goalMonthRandomDay);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("findUserConsumptionGoal : 한달전 ConsumptionGoal만 있을 경우 한달전으로 초기화")
	void findUserConsumptionGoal_previousMonth() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);

		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		given(categoryRepository.findUserCategoryByUserId(user.getId())).willReturn(
			List.of(defaultCategory, userCategory));

		ConsumptionGoal previousMonthDefaultCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay.minusMonths(1))
			.build();

		ConsumptionGoal previousMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(goalMonthRandomDay.minusMonths(1))
			.build();

		List<ConsumptionGoal> previousGoalList = List.of(previousMonthDefaultCategoryGoal,
			previousMonthUserCategoryGoal);

		given(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(),
			GOAL_MONTH.minusMonths(1))).willReturn(previousGoalList);

		List<ConsumptionGoalResponseDto> expected = previousGoalList.stream()
			.map(consumptionGoalConverter::toConsumptionGoalResponseDto)
			.toList();

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoal(user.getId(),
			goalMonthRandomDay);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("findUserConsumptionGoal : 한달 전과 목표 달 ConsumptionGoal이 있을 경우 목표 달로 초기화")
	void findUserConsumptionGoal_previousMonthAndGoalMonth() {
		// given
		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		ConsumptionGoal previousMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(goalMonthRandomDay.minusMonths(1))
			.build();

		ConsumptionGoal goalMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(2_000_000L)
			.consumeAmount(30_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(goalMonthRandomDay)
			.build();

		given(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(),
			GOAL_MONTH.minusMonths(1))).willReturn(List.of(previousMonthUserCategoryGoal));

		given(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(), GOAL_MONTH)).willReturn(
			List.of(goalMonthUserCategoryGoal));

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoal(user.getId(),
			goalMonthRandomDay);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison()
			.isEqualTo(List.of(consumptionGoalConverter.toConsumptionGoalResponseDto(goalMonthUserCategoryGoal)));
	}

	@Test
	@DisplayName("updateConsumptionGoal : 이번달 목표가 있는 경우(defaultCategory)와 목표가 없는 경우(userCategory)")
	void updateConsumptionGoal_Success() {
		// given
		Long defaultGoalAmount = 100L;
		Long userGoalAmount = 200L;

		ConsumptionGoalListRequestDto request = new ConsumptionGoalListRequestDto(
			List.of(new ConsumptionGoalRequestDto(-1L, defaultGoalAmount),
				new ConsumptionGoalRequestDto(-2L, userGoalAmount)));

		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);
		given(categoryRepository.findById(defaultCategory.getId())).willReturn(Optional.of(defaultCategory));

		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);
		given(categoryRepository.findById(userCategory.getId())).willReturn(Optional.of(userCategory));

		ConsumptionGoal defaultCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(GOAL_MONTH)
			.build();
		given(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, defaultCategory,
			GOAL_MONTH)).willReturn(Optional.ofNullable(defaultCategoryGoal));

		given(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, userCategory,
			GOAL_MONTH)).willReturn(Optional.ofNullable(null));

		when(consumptionGoalRepository.saveAll(any())).thenAnswer(invocation -> {
			List<ConsumptionGoal> goalsToSave = invocation.getArgument(0);
			return goalsToSave;
		});

		List<ConsumptionGoalResponseDto> expected = List.of(ConsumptionGoalResponseDto.builder()
			.goalAmount(defaultGoalAmount)
			.consumeAmount(defaultCategoryGoal.getConsumeAmount())
			.categoryName(defaultCategory.getName())
			.categoryId(defaultCategory.getId())
			.build(), ConsumptionGoalResponseDto.builder()
			.goalAmount(userGoalAmount)
			.consumeAmount(0L)
			.categoryName(userCategory.getName())
			.categoryId(userCategory.getId())
			.build());

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.updateConsumptionGoals(user.getId(), request);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("getPeerInfo : 또래 나이와 성별 정보를 통해 PeerInfo 조회 성공")
	void getPeerInfo_Success() {
		// given
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// when
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		String peerGender = "MALE";

		PeerInfoResponseDTO result = consumptionGoalService.getPeerInfo(user.getId(), peerAgeStart, peerAgeEnd,
			peerGender);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getPeerAgeStart()).isEqualTo(peerAgeStart);
		assertThat(result.getPeerAgeEnd()).isEqualTo(peerAgeEnd);
		assertThat(result.getPeerGender()).isEqualTo("MALE");
	}

	@Test
	@DisplayName("getPeerInfo : 유저를 찾을 수 없음")
	void getPeerInfo_UserNotFound() {
		// given
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		// when
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		String peerGender = "MALE";

		// then
		assertThrows(NoSuchElementException.class, () -> {
			consumptionGoalService.getPeerInfo(user.getId(), peerAgeStart, peerAgeEnd, peerGender);
		});
	}

	@Test
	@DisplayName("getTopCategoryAndConsumptionAmount : 가장 큰 계획 카테고리와 이번 주 소비 금액 조회 성공")
	void getTopCategoryAndConsumptionAmount_Success() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);

		ConsumptionGoal topConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(5000L)
			.consumeAmount(3000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay)
			.build();

		ConsumptionGoal currentWeekConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(5000L)
			.consumeAmount(2000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay)
			.build();

		LocalDate startOfWeek = goalMonthRandomDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = goalMonthRandomDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
		given(consumptionGoalRepository.findTopCategoriesAndGoalAmount(1, 23, 25, Gender.MALE))
			.willReturn(List.of(topConsumptionGoal));
		given(consumptionGoalRepository.findTopConsumptionByCategoryIdAndCurrentWeek(
			defaultCategory.getId(), startOfWeek, endOfWeek))
			.willReturn(Optional.of(currentWeekConsumptionGoal));

		// when
		ConsumptionAnalysisResponseDTO result = consumptionGoalService.getTopCategoryAndConsumptionAmount(user.getId());

		// then
		assertThat(result.getGoalCategory()).isEqualTo(defaultCategory.getName());
		assertThat(result.getCurrentWeekConsumptionAmount()).isEqualTo(currentWeekConsumptionGoal.getConsumeAmount());
	}
}