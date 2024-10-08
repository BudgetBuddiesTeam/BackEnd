package com.bbteam.budgetbuddies.domain.consumptiongoal.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.converter.ConsumptionGoalConverter;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AllConsumptionCategoryResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionAnalysisResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalListRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalRequestDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.ConsumptionGoalResponseListDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.PeerInfoResponseDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.TopCategoryConsumptionDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

@DisplayName("ConsumptionGoalImpl 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
class ConsumptionGoalServiceTest {
	private final LocalDate GOAL_MONTH = LocalDate.of(2024, 07, 01);
	private final LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
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

		user = Mockito.spy(User.builder()
			.email("email")
			.age(24)
			.name("name")
			.gender(Gender.MALE)
			.phoneNumber("010-1234-5678")
			.build());
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

		List<ConsumptionGoalResponseDto> expected = List.of(
			new ConsumptionGoalResponseDto(defaultCategory.getName(), defaultCategory.getId(), 0L, 0L),
			new ConsumptionGoalResponseDto(userCategory.getName(), userCategory.getId(), 0L, 0L));

		// when
		when(categoryRepository.findUserCategoryByUserId(user.getId())).thenReturn(categoryList);

		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
			goalMonthRandomDay);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
		assertEquals(result.getTotalRemainingBalance(), 0L);
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
			.consumeAmount(200_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay.minusMonths(1))
			.build();
		Long previousMonthDefaultGoalRemainingBalance =
			previousMonthDefaultCategoryGoal.getGoalAmount() - previousMonthDefaultCategoryGoal.getConsumeAmount();

		ConsumptionGoal previousMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(goalMonthRandomDay.minusMonths(1))
			.build();
		Long previousMonthUseGoalRemainingBalance =
			previousMonthUserCategoryGoal.getGoalAmount() - previousMonthUserCategoryGoal.getConsumeAmount();

		List<ConsumptionGoal> previousGoalList = List.of(previousMonthDefaultCategoryGoal,
			previousMonthUserCategoryGoal);

		List<ConsumptionGoalResponseDto> expected = List.of(
			consumptionGoalConverter.toConsumptionGoalResponseDto(previousMonthUserCategoryGoal),
			consumptionGoalConverter.toConsumptionGoalResponseDto(previousMonthDefaultCategoryGoal));

		// when
		when(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(),
			GOAL_MONTH.minusMonths(1))).thenReturn(previousGoalList);

		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
			goalMonthRandomDay);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
		assertEquals(result.getTotalRemainingBalance(),
			previousMonthDefaultGoalRemainingBalance + previousMonthUseGoalRemainingBalance);
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

		// when
		when(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(),
			GOAL_MONTH.minusMonths(1))).thenReturn(List.of(previousMonthUserCategoryGoal));

		when(consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(user.getId(), GOAL_MONTH)).thenReturn(
			List.of(goalMonthUserCategoryGoal));

		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
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
		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);

		given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));

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
			.goalMonth(thisMonth)
			.build();
		given(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, defaultCategory,
			thisMonth)).willReturn(Optional.ofNullable(defaultCategoryGoal));

		given(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, userCategory,
			thisMonth)).willReturn(Optional.ofNullable(null));

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

		PeerInfoResponseDto result = consumptionGoalService.getPeerInfo(user.getId(), peerAgeStart, peerAgeEnd,
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
		Category defaultCategory = Mockito.spy(Category.builder()
			.name("디폴트 카테고리")
			.user(null)
			.isDefault(true)
			.build());
		given(defaultCategory.getId()).willReturn(1L);

		LocalDate goalMonthRandomDay = LocalDate.now();
		LocalDate startOfWeek = goalMonthRandomDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate endOfWeek = goalMonthRandomDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
		LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);

		ConsumptionGoal topConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(5000L)
			.consumeAmount(3000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay.minusWeeks(1))
			.build();

		ConsumptionGoal currentWeekConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(5000L)
			.consumeAmount(2000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(goalMonthRandomDay)
			.build();

		List<AvgConsumptionGoalDto> avgConsumptionGoalList = List.of(
			new AvgConsumptionGoalDto(defaultCategory.getId(), 5000L)
		);

		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;

		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
		given(consumptionGoalRepository.findAvgGoalAmountByCategory(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth))
			.willReturn(avgConsumptionGoalList);

		given(consumptionGoalRepository.findAvgConsumptionByCategoryIdAndCurrentWeek(
			defaultCategory.getId(), startOfWeekDateTime, endOfWeekDateTime,
			peerAgeStart, peerAgeEnd, peerGender))
			.willReturn(Optional.of(currentWeekConsumptionGoal.getConsumeAmount()));

		given(categoryRepository.findById(defaultCategory.getId()))
			.willReturn(Optional.of(defaultCategory));

		// when
		ConsumptionAnalysisResponseDto result = consumptionGoalService.getTopCategoryAndConsumptionAmount(user.getId());

		// then
		assertThat(result.getGoalCategory()).isEqualTo(defaultCategory.getName());
		assertThat(result.getCurrentWeekConsumptionAmount()).isEqualTo(currentWeekConsumptionGoal.getConsumeAmount());
	}

	@Test
	@DisplayName("getTopConsumptionCategories : 또래들이 가장 많이 소비한 카테고리 top3 조회 성공")
	void getTopConsumptionCategories_Success() {
		// given
		Category defaultCategory1 = Mockito.mock(Category.class);
		Category defaultCategory2 = Mockito.mock(Category.class);
		Category defaultCategory3 = Mockito.mock(Category.class);

		given(defaultCategory1.getId()).willReturn(1L);
		given(defaultCategory2.getId()).willReturn(2L);
		given(defaultCategory3.getId()).willReturn(3L);

		given(defaultCategory1.getName()).willReturn("디폴트 카테고리");
		given(defaultCategory2.getName()).willReturn("디폴트 카테고리2");
		given(defaultCategory3.getName()).willReturn("디폴트 카테고리3");

		CategoryConsumptionCountDto topConsumption1 = new CategoryConsumptionCountDto(defaultCategory1.getId(), 30L);
		CategoryConsumptionCountDto topConsumption2 = new CategoryConsumptionCountDto(defaultCategory2.getId(), 25L);
		CategoryConsumptionCountDto topConsumption3 = new CategoryConsumptionCountDto(defaultCategory3.getId(), 20L);

		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		String peerGender = "MALE";

		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
		given(consumptionGoalRepository.findTopCategoriesByConsumptionCount(peerAgeStart, peerAgeEnd,
			Gender.valueOf(peerGender), currentMonth.atStartOfDay()))
			.willReturn(List.of(topConsumption1, topConsumption2, topConsumption3));

		given(categoryRepository.findById(defaultCategory1.getId())).willReturn(Optional.of(defaultCategory1));
		given(categoryRepository.findById(defaultCategory2.getId())).willReturn(Optional.of(defaultCategory2));
		given(categoryRepository.findById(defaultCategory3.getId())).willReturn(Optional.of(defaultCategory3));

		// when
		List<TopCategoryConsumptionDto> result = consumptionGoalService.getTopConsumptionCategories(
			user.getId(), peerAgeStart, peerAgeEnd, peerGender);

		// then
		assertThat(result).hasSize(3);

		assertThat(result.get(0).getCategoryName()).isEqualTo(defaultCategory1.getName());
		assertThat(result.get(0).getConsumptionCount()).isEqualTo(topConsumption1.getConsumptionCount());

		assertThat(result.get(1).getCategoryName()).isEqualTo(defaultCategory2.getName());
		assertThat(result.get(1).getConsumptionCount()).isEqualTo(topConsumption2.getConsumptionCount());

		assertThat(result.get(2).getCategoryName()).isEqualTo(defaultCategory3.getName());
		assertThat(result.get(2).getConsumptionCount()).isEqualTo(topConsumption3.getConsumptionCount());
	}

	@Test
	@DisplayName("getAllConsumptionGoalCategories : 또래들이 세운 가장 큰 목표 카테고리 전체조회 성공")
	void getAllConsumptionGoalCategories_Success() {
		// given
		Category defaultCategory1 = new Category();
		Category defaultCategory2 = new Category();

		defaultCategory1.setId(1L);
		defaultCategory1.setName("디폴트 카테고리1");
		defaultCategory1.setIsDefault(true);

		defaultCategory2.setId(2L);
		defaultCategory2.setName("디폴트 카테고리2");
		defaultCategory2.setIsDefault(true);

		List<AvgConsumptionGoalDto> categoryAvgList = List.of(
			new AvgConsumptionGoalDto(1L, 3000L),
			new AvgConsumptionGoalDto(2L, 4000L)
		);

		List<MyConsumptionGoalDto> myConsumptionAmountList = List.of(
			new MyConsumptionGoalDto(1L, 5000L),
			new MyConsumptionGoalDto(2L, 2000L)
		);

		List<Category> defaultCategories = List.of(defaultCategory1, defaultCategory2);

		given(categoryRepository.findAllByIsDefaultTrue()).willReturn(defaultCategories);
		given(consumptionGoalRepository.findAvgGoalAmountByCategory(
			anyInt(), anyInt(), any(), any())).willReturn(categoryAvgList);
		given(consumptionGoalRepository.findAllGoalAmountByUserId(user.getId()))
			.willReturn(myConsumptionAmountList);
		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

		// when
		List<AllConsumptionCategoryResponseDto> result = consumptionGoalService.getAllConsumptionGoalCategories(
			user.getId(), 23, 25, "MALE");

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);

		AllConsumptionCategoryResponseDto firstCategory = result.get(0);
		assertThat(firstCategory.getCategoryName()).isEqualTo("디폴트 카테고리1");
		assertThat(firstCategory.getAvgAmount()).isEqualTo(3000L);
		assertThat(firstCategory.getAmountDifference()).isEqualTo(2000L);

		AllConsumptionCategoryResponseDto secondCategory = result.get(1);
		assertThat(secondCategory.getCategoryName()).isEqualTo("디폴트 카테고리2");
		assertThat(secondCategory.getAvgAmount()).isEqualTo(4000L);
		assertThat(secondCategory.getAmountDifference()).isEqualTo(-2000L);
	}

	@Test
	@DisplayName("getAllConsumptionCategories :  또래들이 가장 많이 소비한 카테고리 전체조회 성공")
	void getAllConsumptionCategories_Success() {
		// given
		Category defaultCategory1 = new Category();
		defaultCategory1.setId(1L);
		defaultCategory1.setName("디폴트 카테고리1");
		defaultCategory1.setIsDefault(true);

		Category defaultCategory2 = new Category();
		defaultCategory2.setId(2L);
		defaultCategory2.setName("디폴트 카테고리2");
		defaultCategory2.setIsDefault(true);

		List<AvgConsumptionGoalDto> categoryAvgList = List.of(
			new AvgConsumptionGoalDto(1L, 3000L),
			new AvgConsumptionGoalDto(2L, 4000L)
		);

		List<MyConsumptionGoalDto> myConsumptionAmountList = List.of(
			new MyConsumptionGoalDto(1L, 5000L),
			new MyConsumptionGoalDto(2L, 2000L)
		);

		List<Category> defaultCategories = List.of(defaultCategory1, defaultCategory2);

		given(categoryRepository.findAllByIsDefaultTrue()).willReturn(defaultCategories);
		given(consumptionGoalRepository.findAvgConsumptionAmountByCategory(
			anyInt(), anyInt(), any(), any())).willReturn(categoryAvgList);
		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

		// when
		List<AllConsumptionCategoryResponseDto> result = consumptionGoalService.getAllConsumptionCategories(
			user.getId(), 23, 25, "MALE");

		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);

		AllConsumptionCategoryResponseDto firstCategory = result.get(0);
		assertThat(firstCategory.getCategoryName()).isEqualTo("디폴트 카테고리1");
		assertThat(firstCategory.getAvgAmount()).isEqualTo(3000L);
		assertThat(firstCategory.getAmountDifference()).isEqualTo(-3000);

		AllConsumptionCategoryResponseDto secondCategory = result.get(1);
		assertThat(secondCategory.getCategoryName()).isEqualTo("디폴트 카테고리2");
		assertThat(secondCategory.getAvgAmount()).isEqualTo(4000L);
		assertThat(secondCategory.getAmountDifference()).isEqualTo(-4000L);
	}

	@Test
	@DisplayName("지난 달, 이번 달 소비 목표가 없는 카테고리에 대한 소비 업데이트를 진행하는 경우 새로운 소비 목표를 생성해 소비 금액을 갱신")
	void recalculateConsumptionAmount_notExistPreviousMonthAndThisMonthGoal() {
		// given
		Category existGoalCategory = Category.builder().name("유저 카테고리").user(user).isDefault(false).build();
		Category notExistGoalCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").isDefault(true).build());
		given(notExistGoalCategory.getId()).willReturn(-1L);

		Expense expense = Mockito.spy(
			Expense.builder().category(existGoalCategory).expenseDate(GOAL_MONTH.atStartOfDay()).amount(1000L).build());
		when(expense.getId()).thenReturn(-1L);

		ExpenseUpdateRequestDto request = ExpenseUpdateRequestDto.builder()
			.amount(1000L)
			.expenseId(expense.getId())
			.expenseDate(LocalDate.of(2024, 8, 7).atStartOfDay())
			.categoryId(notExistGoalCategory.getId())
			.build();

		ConsumptionGoal oldGoal = ConsumptionGoal.builder().consumeAmount(1000L).category(existGoalCategory).build();
		ConsumptionGoal expected = ConsumptionGoal.builder()
			.goalMonth(LocalDate.of(2024, 8, 1))
			.goalAmount(0L)
			.consumeAmount(1000L)
			.category(notExistGoalCategory)
			.user(user)
			.build();
		// when
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, expense.getCategory(),
			expense.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(Optional.ofNullable(oldGoal));

		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(notExistGoalCategory));
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, notExistGoalCategory,
			request.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(Optional.empty());

		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, notExistGoalCategory,
			request.getExpenseDate().minusMonths(1).toLocalDate().withDayOfMonth(1))).thenReturn(Optional.empty());

		consumptionGoalService.recalculateConsumptionAmount(expense, request, user);

		ArgumentCaptor<ConsumptionGoal> consumptionGoalCaptor = ArgumentCaptor.forClass(ConsumptionGoal.class);
		verify(consumptionGoalRepository, times(2)).save(consumptionGoalCaptor.capture());

		List<ConsumptionGoal> savedConsumptionGoals = consumptionGoalCaptor.getAllValues();

		// then
		assertEquals(oldGoal.getConsumeAmount(), 0L);
		assertThat(savedConsumptionGoals.get(1)).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("이번달 소비 목표가 없는 카테고리에 대한 소비 업데이트를 진행하는 경우 지난 달 소비 목표의 목표 금액을 복사한 이번 달 소비 목표를 생성해 소비 금액을 갱신")
	void recalculateConsumptionAmount_notExistThisMonthGoal() {
		// given
		Category existGoalCategory = Category.builder().name("유저 카테고리").user(user).isDefault(false).build();
		Category notExistThisMonthGoalCategory = Mockito.spy(
			Category.builder().name("디폴트 카테고리").isDefault(true).build());
		given(notExistThisMonthGoalCategory.getId()).willReturn(-1L);

		Expense expense = Mockito.spy(
			Expense.builder().category(existGoalCategory).expenseDate(GOAL_MONTH.atStartOfDay()).amount(1000L).build());
		when(expense.getId()).thenReturn(-1L);

		ExpenseUpdateRequestDto request = ExpenseUpdateRequestDto.builder()
			.amount(1000L)
			.expenseId(expense.getId())
			.expenseDate(LocalDate.of(2024, 8, 7).atStartOfDay())
			.categoryId(notExistThisMonthGoalCategory.getId())
			.build();

		ConsumptionGoal oldGoal = ConsumptionGoal.builder().consumeAmount(1000L).category(existGoalCategory).build();

		ConsumptionGoal previousMonthGoal = ConsumptionGoal.builder()
			.goalMonth(LocalDate.of(2024, 7, 1))
			.goalAmount(3000L)
			.consumeAmount(3000L)
			.category(notExistThisMonthGoalCategory)
			.user(user)
			.build();

		ConsumptionGoal expected = ConsumptionGoal.builder()
			.goalMonth(LocalDate.of(2024, 8, 1))
			.goalAmount(3000L)
			.consumeAmount(1000L)
			.category(notExistThisMonthGoalCategory)
			.user(user)
			.build();

		// when
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, expense.getCategory(),
			expense.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(Optional.ofNullable(oldGoal));

		when(categoryRepository.findById(request.getCategoryId())).thenReturn(
			Optional.of(notExistThisMonthGoalCategory));
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user,
			notExistThisMonthGoalCategory, request.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(
			Optional.empty());

		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user,
			notExistThisMonthGoalCategory,
			request.getExpenseDate().minusMonths(1).toLocalDate().withDayOfMonth(1))).thenReturn(
			Optional.ofNullable(previousMonthGoal));

		consumptionGoalService.recalculateConsumptionAmount(expense, request, user);

		ArgumentCaptor<ConsumptionGoal> consumptionGoalCaptor = ArgumentCaptor.forClass(ConsumptionGoal.class);
		verify(consumptionGoalRepository, times(2)).save(consumptionGoalCaptor.capture());

		List<ConsumptionGoal> savedConsumptionGoals = consumptionGoalCaptor.getAllValues();

		// then
		assertEquals(oldGoal.getConsumeAmount(), 0L);
		assertThat(savedConsumptionGoals.get(1)).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("이번달 소비 목표가 있는 경우 이번 달 소비 목표의 소비 금액을 갱신")
	void recalculateConsumptionAmount_existThisMonthGoal() {
		// given
		Category existGoalCategory = Mockito.spy(
			Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(existGoalCategory.getId()).willReturn(-1L);

		Expense expense = Mockito.spy(
			Expense.builder().category(existGoalCategory).expenseDate(GOAL_MONTH.atStartOfDay()).amount(1000L).build());
		when(expense.getId()).thenReturn(-1L);

		ExpenseUpdateRequestDto request = ExpenseUpdateRequestDto.builder()
			.amount(2000L)
			.expenseId(expense.getId())
			.expenseDate(LocalDate.of(2024, 8, 7).atStartOfDay())
			.categoryId(existGoalCategory.getId())
			.build();

		ConsumptionGoal oldGoal = ConsumptionGoal.builder()
			.goalMonth(LocalDate.of(2024, 8, 1))
			.goalAmount(3000L)
			.consumeAmount(1000L)
			.category(existGoalCategory)
			.user(user)
			.build();

		ConsumptionGoal expected = ConsumptionGoal.builder()
			.goalMonth(LocalDate.of(2024, 8, 1))
			.goalAmount(3000L)
			.consumeAmount(2000L)
			.category(existGoalCategory)
			.user(user)
			.build();

		// when
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, expense.getCategory(),
			expense.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(Optional.ofNullable(oldGoal));

		when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(existGoalCategory));
		when(consumptionGoalRepository.findConsumptionGoalByUserAndCategoryAndGoalMonth(user, existGoalCategory,
			request.getExpenseDate().toLocalDate().withDayOfMonth(1))).thenReturn(Optional.ofNullable(oldGoal));

		consumptionGoalService.recalculateConsumptionAmount(expense, request, user);

		// then
		assertThat(oldGoal).usingRecursiveComparison().isEqualTo(expected);
	}
}