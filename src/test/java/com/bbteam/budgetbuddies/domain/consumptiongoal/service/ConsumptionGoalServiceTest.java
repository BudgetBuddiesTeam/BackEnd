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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.category.service.CategoryService;
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
import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.domain.user.service.UserService;
import com.bbteam.budgetbuddies.enums.Gender;

@DisplayName("ConsumptionGoalImpl 서비스 테스트의 ")
@ExtendWith(MockitoExtension.class)
class ConsumptionGoalServiceTest {
	private final LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
	private User user;
	private LocalDate requestMonth;
	@InjectMocks
	private ConsumptionGoalServiceImpl consumptionGoalService;
	@Mock
	private ConsumptionGoalRepository consumptionGoalRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private CategoryService categoryService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserService userService;
	@Mock
	private ExpenseRepository expenseRepository;
	@Spy
	private ConsumptionGoalConverter consumptionGoalConverter;

	@BeforeEach
	void setUp() {
		requestMonth = LocalDate.of(2024, 7, 15);

		user = Mockito.spy(User.builder()
			.email("email")
			.age(24)
			.name("name")
			.gender(Gender.MALE)
			.phoneNumber("010-1234-5678")
			.build());
	}

	@Test
	void 유저소비목표조회시_이번달_이전달_소비목표가_없는_경우_소비목표를_새로_생성() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);
		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		given(categoryService.getUserCategoryList(user.getId())).willReturn(List.of(defaultCategory, userCategory));
		given(consumptionGoalRepository.save(any(ConsumptionGoal.class))).will(AdditionalAnswers.returnsFirstArg());

		List<ConsumptionGoalResponseDto> expected = List.of(
			new ConsumptionGoalResponseDto(defaultCategory.getName(), defaultCategory.getId(), 0L, 0L),
			new ConsumptionGoalResponseDto(userCategory.getName(), userCategory.getId(), 0L, 0L));

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
			requestMonth);

		// then
		assertThat(result.getConsumptionGoalList()).usingRecursiveComparison().isEqualTo(expected);
		assertEquals(result.getTotalRemainingBalance(), 0L);
	}

	@Test
	void 소비목표조회시_지난달_소비목표만_존재할시_지난달_기준으로_소비목표를_생성() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);
		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		given(categoryService.getUserCategoryList(user.getId())).willReturn(List.of(defaultCategory, userCategory));
		given(userService.getUser(user.getId())).willReturn(user);

		ConsumptionGoal previousMonthDefaultCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(200_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(requestMonth.withDayOfMonth(1).minusMonths(1))
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, defaultCategory,
			requestMonth.withDayOfMonth(1))).willReturn(Optional.empty());
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, defaultCategory,
			requestMonth.withDayOfMonth(1).minusMonths(1))).willReturn(Optional.of(previousMonthDefaultCategoryGoal));

		ConsumptionGoal previousMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(requestMonth.withDayOfMonth(1).minusMonths(1))
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, userCategory,
			requestMonth.withDayOfMonth(1))).willReturn(Optional.empty());
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, userCategory,
			requestMonth.withDayOfMonth(1).minusMonths(1))).willReturn(Optional.of(previousMonthUserCategoryGoal));

		given(consumptionGoalRepository.save(any(ConsumptionGoal.class))).will(AdditionalAnswers.returnsFirstArg());

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
			requestMonth);

		// then
		assertThat(result.getTotalRemainingBalance()).isEqualTo(2_000_000L);
		assertThat(result.getTotalConsumptionAmount()).isEqualTo(0L);
	}

	@Test
	void 소비목표_조회시_이번달_소비목표가_존재하는_경우_이번달_소비목표를_반환() {
		// given
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);
		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").user(user).isDefault(false).build());
		given(userCategory.getId()).willReturn(-2L);

		given(categoryService.getUserCategoryList(user.getId())).willReturn(List.of(defaultCategory, userCategory));
		given(userService.getUser(user.getId())).willReturn(user);

		ConsumptionGoal thisMonthDefaultCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(200_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(requestMonth.withDayOfMonth(1))
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, defaultCategory,
			requestMonth.withDayOfMonth(1))).willReturn(Optional.of(thisMonthDefaultCategoryGoal));

		ConsumptionGoal thisMonthUserCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(userCategory)
			.goalMonth(requestMonth.withDayOfMonth(1))
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, userCategory,
			requestMonth.withDayOfMonth(1))).willReturn(Optional.of(thisMonthUserCategoryGoal));

		// when
		ConsumptionGoalResponseListDto result = consumptionGoalService.findUserConsumptionGoalList(user.getId(),
			requestMonth);

		// then
		assertThat(result.getTotalRemainingBalance()).isEqualTo(1_780_000L);
		assertThat(result.getTotalConsumptionAmount()).isEqualTo(220_000L);
	}

	@Test
	void 소비목표_업데이트_성공() {
		// given
		Long updateAmount = 100L;
		LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);

		given(userService.getUser(user.getId())).willReturn(user);

		ConsumptionGoalListRequestDto request = new ConsumptionGoalListRequestDto(
			List.of(new ConsumptionGoalRequestDto(-1L, updateAmount)));

		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
		given(defaultCategory.getId()).willReturn(-1L);
		given(categoryService.getCategory(defaultCategory.getId())).willReturn(defaultCategory);

		ConsumptionGoal defaultCategoryGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(20_000L)
			.user(user)
			.category(defaultCategory)
			.goalMonth(thisMonth)
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, defaultCategory, thisMonth)).willReturn(
			Optional.ofNullable(defaultCategoryGoal));

		List<ConsumptionGoalResponseDto> expected = List.of(ConsumptionGoalResponseDto.builder()
			.goalAmount(updateAmount)
			.consumeAmount(defaultCategoryGoal.getConsumeAmount())
			.categoryName(defaultCategory.getName())
			.categoryId(defaultCategory.getId())
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
		Category defaultCategory = Mockito.spy(Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());
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
			new AvgConsumptionGoalDto(defaultCategory.getId(), 5000L));

		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;

		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
		given(consumptionGoalRepository.findAvgGoalAmountByCategory(peerAgeStart, peerAgeEnd, peerGender,
			currentMonth)).willReturn(avgConsumptionGoalList);

		given(consumptionGoalRepository.findAvgConsumptionByCategoryIdAndCurrentWeek(defaultCategory.getId(),
			startOfWeekDateTime, endOfWeekDateTime, peerAgeStart, peerAgeEnd, peerGender)).willReturn(
			Optional.of(currentWeekConsumptionGoal.getConsumeAmount()));

		given(categoryRepository.findById(defaultCategory.getId())).willReturn(Optional.of(defaultCategory));

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
		given(
			expenseRepository.findTopCategoriesByConsumptionCount(peerAgeStart, peerAgeEnd, Gender.valueOf(peerGender),
				currentMonth.atStartOfDay())).willReturn(List.of(topConsumption1, topConsumption2, topConsumption3));

		given(categoryRepository.findById(defaultCategory1.getId())).willReturn(Optional.of(defaultCategory1));
		given(categoryRepository.findById(defaultCategory2.getId())).willReturn(Optional.of(defaultCategory2));
		given(categoryRepository.findById(defaultCategory3.getId())).willReturn(Optional.of(defaultCategory3));

		// when
		List<TopCategoryConsumptionDto> result = consumptionGoalService.getTopConsumptionCategories(user.getId(),
			peerAgeStart, peerAgeEnd, peerGender);

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

		List<AvgConsumptionGoalDto> categoryAvgList = List.of(new AvgConsumptionGoalDto(1L, 3000L),
			new AvgConsumptionGoalDto(2L, 4000L));

		List<MyConsumptionGoalDto> myConsumptionAmountList = List.of(new MyConsumptionGoalDto(1L, 5000L),
			new MyConsumptionGoalDto(2L, 2000L));

		List<Category> defaultCategories = List.of(defaultCategory1, defaultCategory2);

		given(categoryRepository.findAllByIsDefaultTrue()).willReturn(defaultCategories);
		given(consumptionGoalRepository.findAvgGoalAmountByCategory(anyInt(), anyInt(), any(), any())).willReturn(
			categoryAvgList);
		given(consumptionGoalRepository.findAllGoalAmountByUserId(user.getId(), currentMonth)).willReturn(
			myConsumptionAmountList);
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

		List<AvgConsumptionGoalDto> categoryAvgList = List.of(new AvgConsumptionGoalDto(1L, 3000L),
			new AvgConsumptionGoalDto(2L, 4000L));

		List<MyConsumptionGoalDto> myConsumptionAmountList = List.of(new MyConsumptionGoalDto(1L, 5000L),
			new MyConsumptionGoalDto(2L, 2000L));

		List<Category> defaultCategories = List.of(defaultCategory1, defaultCategory2);

		given(categoryRepository.findAllByIsDefaultTrue()).willReturn(defaultCategories);
		given(
			consumptionGoalRepository.findAvgConsumptionAmountByCategory(anyInt(), anyInt(), any(), any())).willReturn(
			categoryAvgList);
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
	void 이번달_사용자_소비목표_조회하기() {
		// given
		Category category = Mockito.spy(Category.builder().name("TEST CATEGORY").user(user).isDefault(false).build());
		LocalDate goalMonth = LocalDate.of(2024, 7, 1);

		ConsumptionGoal userConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(200_000L)
			.user(user)
			.category(category)
			.goalMonth(goalMonth)
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, category, goalMonth)).willReturn(
			Optional.of(userConsumptionGoal));

		// when
		ConsumptionGoal result = consumptionGoalService.getUserConsumptionGoal(user, category, goalMonth);

		// then
		assertEquals(result, userConsumptionGoal);
	}

	@Test
	void 이번달_소비목표가_없는경우_저번달_기준으로_소비목표_생성() {
		// given
		Category category = Mockito.spy(Category.builder().name("TEST CATEGORY").user(user).isDefault(false).build());
		LocalDate goalMonth = LocalDate.of(2024, 7, 1);

		ConsumptionGoal beforeUserConsumptionGoal = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(200_000L)
			.user(user)
			.category(category)
			.goalMonth(LocalDate.of(2024, 6, 1))
			.build();
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, category, goalMonth)).willReturn(
			Optional.empty());
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, category,
			goalMonth.minusMonths(1))).willReturn(Optional.of(beforeUserConsumptionGoal));
		given(consumptionGoalRepository.save(any(ConsumptionGoal.class))).will(AdditionalAnswers.returnsFirstArg());

		ConsumptionGoal expected = ConsumptionGoal.builder()
			.goalAmount(1_000_000L)
			.consumeAmount(0L)
			.user(user)
			.category(category)
			.goalMonth(LocalDate.of(2024, 7, 1))
			.build();
		// when
		ConsumptionGoal result = consumptionGoalService.getUserConsumptionGoal(user, category, goalMonth);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	void 이번달_저번달_소비목표가_없는경우_소비목표를_새로_생성() {
		// given
		Category category = Mockito.spy(Category.builder().name("TEST CATEGORY").user(user).isDefault(false).build());
		LocalDate goalMonth = LocalDate.of(2024, 7, 1);

		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, category, goalMonth)).willReturn(
			Optional.empty());
		given(consumptionGoalRepository.findByUserAndCategoryAndGoalMonth(user, category,
			goalMonth.minusMonths(1))).willReturn(Optional.empty());
		given(consumptionGoalRepository.save(any(ConsumptionGoal.class))).will(AdditionalAnswers.returnsFirstArg());

		ConsumptionGoal expected = ConsumptionGoal.builder()
			.goalAmount(0L)
			.consumeAmount(0L)
			.user(user)
			.category(category)
			.goalMonth(LocalDate.of(2024, 7, 1))
			.build();
		// when
		ConsumptionGoal result = consumptionGoalService.getUserConsumptionGoal(user, category, goalMonth);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}
}