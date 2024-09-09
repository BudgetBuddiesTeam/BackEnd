package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.AvgConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.CategoryConsumptionCountDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.MyConsumptionGoalDto;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;

@DisplayName("ConsumptionGoal 레포지토리 테스트의 ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConsumptionGoalRepositoryTest {
	@Autowired
	ConsumptionGoalRepository consumptionGoalRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ExpenseRepository expenseRepository;

	private User peerUser1;
	private User peerUser2;
	private Category defaultCategory1;
	private Category defaultCategory2;
	private LocalDate currentMonth;
	private LocalDateTime now = LocalDateTime.now();

	@BeforeEach
	void setUp() {
		defaultCategory1 = categoryRepository.save(
			Category.builder().name("Category 1").user(null).isDefault(true).build());

		defaultCategory2 = categoryRepository.save(
			Category.builder().name("Category 2").user(null).isDefault(true).build());

		peerUser1 = userRepository.save(
			User.builder()
				.email("peer1@example.com")
				.age(24)
				.name("Peer User 1")
				.gender(Gender.MALE)
				.phoneNumber("010-1111-1111")
				.build());

		peerUser2 = userRepository.save(
			User.builder()
				.email("peer2@example.com")
				.age(25)
				.name("Peer User 2")
				.gender(Gender.MALE)
				.phoneNumber("010-2222-2222")
				.build());

		currentMonth = LocalDate.now();

		consumptionGoalRepository.save(
			ConsumptionGoal.builder()
				.goalAmount(100L)
				.consumeAmount(50L)
				.user(peerUser1)
				.category(defaultCategory1)
				.goalMonth(currentMonth)
				.build());

		consumptionGoalRepository.save(
			ConsumptionGoal.builder()
				.goalAmount(150L)
				.consumeAmount(100L)
				.user(peerUser1)
				.category(defaultCategory2)
				.goalMonth(currentMonth)
				.build());

		consumptionGoalRepository.save(
			ConsumptionGoal.builder()
				.goalAmount(200L)
				.consumeAmount(150L)
				.user(peerUser2)
				.category(defaultCategory2)
				.goalMonth(currentMonth)
				.build());

		expenseRepository.save(
			Expense.builder()
				.amount(1L)
				.category(defaultCategory1)
				.user(peerUser1)
				.expenseDate(now)
				.build());

		expenseRepository.save(
			Expense.builder()
				.amount(1L)
				.category(defaultCategory1)
				.user(peerUser1)
				.expenseDate(now)
				.build());

		expenseRepository.save(
			Expense.builder()
				.amount(1L)
				.category(defaultCategory2)
				.user(peerUser1)
				.expenseDate(now)
				.build());
	}

	@Test
	@DisplayName("유저 아이디와 goalMonth를 통해 GoalConsumption 조회 성공")
	void findConsumptionGoalByUserIdAndGoalMonth_Success() {
		// given
		// 목표 달
		LocalDate goalMonth = LocalDate.of(2024, 07, 01);

		User mainUser = userRepository.save(
			User.builder().email("email").age(24).name("name").phoneNumber("010-1234-5678").build());

		Category defaultCategory = categoryRepository.save(
			Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());

		Category userCategory = categoryRepository.save(
			Category.builder().name("유저 카테고리").user(mainUser).isDefault(false).build());

		ConsumptionGoal defaultCategoryConsumptionGoal = consumptionGoalRepository.save(ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(mainUser)
			.goalMonth(goalMonth)
			.category(defaultCategory)
			.build());

		ConsumptionGoal userCategoryConsumptionGoal = consumptionGoalRepository.save(ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(mainUser)
			.goalMonth(goalMonth)
			.category(userCategory)
			.build());

		setUnselectedConsumptionGoal(mainUser, goalMonth, defaultCategory);

		// when
		List<ConsumptionGoal> result = consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(
			mainUser.getId(),
			goalMonth);

		// then
		assertThat(result).usingRecursiveComparison()
			.isEqualTo(List.of(defaultCategoryConsumptionGoal, userCategoryConsumptionGoal));
	}

	@Test
	@DisplayName("또래 나이와 성별 정보를 통해 카테고리와 평균 소비 금액 조회 성공")
	void findAvgConsumptionAmountByCategory_Success() {
		// when
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;

		List<AvgConsumptionGoalDto> result = consumptionGoalRepository.findAvgConsumptionAmountByCategory(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth);

		// then
		assertThat(result).isNotEmpty();

		AvgConsumptionGoalDto resultGoal1 = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory1.getId()))
			.findFirst()
			.orElseThrow();
		assertThat(resultGoal1.getAverageAmount()).isEqualTo(50L);

		AvgConsumptionGoalDto resultGoal2 = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory2.getId()))
			.findFirst()
			.orElseThrow();
		assertThat(resultGoal2.getAverageAmount()).isEqualTo(125L);
	}

	@Test
	@DisplayName("또래 나이와 성별 정보를 통해 카테고리와 나의 소비 금액 조회 성공")
	void findAllConsumptionAmountByUserId_Success() {
		// when
		List<MyConsumptionGoalDto> result = consumptionGoalRepository.findAllConsumptionAmountByUserId(
			peerUser1.getId(), currentMonth);

		// then
		assertThat(result).isNotEmpty();

		Long categoryId1 = defaultCategory1.getId();

		MyConsumptionGoalDto firstResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(categoryId1))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + categoryId1 + " not found"));

		assertThat(firstResult.getCategoryId()).isEqualTo(categoryId1);
		assertThat(firstResult.getMyAmount()).isEqualTo(50L);

	}

	private void setUnselectedConsumptionGoal(User mainUser, LocalDate goalMonth, Category defaultCategory) {
		User otherUser = userRepository.save(
			User.builder().email("email2").age(24).name("name2").phoneNumber("010-1567-5678").build());

		ConsumptionGoal lastMonthDefaultCategoryConsumptionGoal = consumptionGoalRepository.save(
			ConsumptionGoal.builder()
				.goalAmount(1L)
				.consumeAmount(1L)
				.user(mainUser)
				.goalMonth(goalMonth.minusMonths(1))
				.category(defaultCategory)
				.build());

		ConsumptionGoal otherUserDefaultCategoryConsumptionGoal = consumptionGoalRepository.save(
			ConsumptionGoal.builder()
				.goalAmount(1L)
				.consumeAmount(1L)
				.user(otherUser)
				.goalMonth(goalMonth)
				.category(defaultCategory)
				.build());
	}

	@Test
	@DisplayName("또래 나이와 성별 정보를 통해 카테고리와 평균 목표 금액 조회 성공")
	void findAvgGoalAmountByCategory_Success() {
		// when
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;

		List<AvgConsumptionGoalDto> result = consumptionGoalRepository.findAvgGoalAmountByCategory(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth);

		// then
		assertThat(result).isNotEmpty();

		AvgConsumptionGoalDto firstResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory1.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory1.getId() + " not found"));

		AvgConsumptionGoalDto secondResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory2.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory2.getId() + " not found"));

		assertThat(firstResult.getAverageAmount()).isEqualTo(100L);
		assertThat(secondResult.getAverageAmount()).isEqualTo(175L);
	}

	@Test
	@DisplayName("또래 나이와 성별 정보를 통해 카테고리와 평균 목표 금액 조회 성공")
	void findAllGoalAmountByUserId_Success() {
		// when
		List<MyConsumptionGoalDto> result = consumptionGoalRepository.findAllGoalAmountByUserId(peerUser1.getId(),
			currentMonth);

		// then
		assertThat(result).isNotEmpty();

		MyConsumptionGoalDto firstResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory1.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory1.getId() + " not found"));

		MyConsumptionGoalDto secondResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory2.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory2.getId() + " not found"));

		assertThat(firstResult.getMyAmount()).isEqualTo(100L);
		assertThat(secondResult.getMyAmount()).isEqualTo(150L);
	}

	@Test
	@DisplayName("또래 나이와 성별 정보를 통해 카테고리별 소비 횟수 조회 성공")
	void findTopCategoriesByConsumptionCount_Success() {
		// when
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;
		LocalDate currentMonth = LocalDate.now();

		List<CategoryConsumptionCountDto> result = expenseRepository.findTopCategoriesByConsumptionCount(
			peerAgeStart, peerAgeEnd, peerGender, currentMonth.atStartOfDay());

		// then
		assertThat(result).isNotEmpty();

		CategoryConsumptionCountDto firstResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory1.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory1.getId() + " not found"));

		CategoryConsumptionCountDto secondResult = result.stream()
			.filter(dto -> dto.getCategoryId().equals(defaultCategory2.getId()))
			.findFirst()
			.orElseThrow(() -> new AssertionError("Category ID " + defaultCategory2.getId() + " not found"));

		assertThat(firstResult.getConsumptionCount()).isEqualTo(2);
		assertThat(secondResult.getConsumptionCount()).isEqualTo(1);
	}
}