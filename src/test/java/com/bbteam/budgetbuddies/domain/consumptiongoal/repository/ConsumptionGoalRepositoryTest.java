package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
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
	@DisplayName("또래 나이와 성별 정보를 통해 GoalConsumption 조회 성공")
	void findTopCategoriesAndGoalAmount_Success() {
		//given
		User mainUser = userRepository.save(
			User.builder()
				.email("email")
				.age(24)
				.name("name")
				.gender(Gender.MALE)
				.phoneNumber("010-1234-5678")
				.build());

		Category defaultCategory = categoryRepository.save(
			Category.builder().name("디폴트 카테고리").user(null).isDefault(true).build());

		LocalDate goalMonth = LocalDate.of(2024, 07, 01);

		ConsumptionGoal defaultCategoryConsumptionGoal = consumptionGoalRepository.save(ConsumptionGoal.builder()
			.goalAmount(1L)
			.consumeAmount(1L)
			.user(mainUser)
			.goalMonth(goalMonth)
			.category(defaultCategory)
			.build());

		// when
		int top = 4;
		int peerAgeStart = 23;
		int peerAgeEnd = 25;
		Gender peerGender = Gender.MALE;

		List<ConsumptionGoal> result = consumptionGoalRepository.findTopCategoriesAndGoalAmount(
			top, peerAgeStart, peerAgeEnd, peerGender);

		// then
		ConsumptionGoal resultGoal = result.get(0);
		assertThat(resultGoal.getGoalAmount()).isEqualTo(1L);
		assertThat(resultGoal.getConsumeAmount()).isEqualTo(1L);
		assertThat(resultGoal.getUser().getAge()).isEqualTo(24);
		assertThat(resultGoal.getCategory().getName()).isEqualTo("디폴트 카테고리");
		assertThat(resultGoal.getUser().getGender()).isEqualTo(Gender.MALE);
	}

	@Test
	@DisplayName("또래들이 가장 큰 목표로 세운 카테고리와 그 카테고리에서 이번 주의 소비 목표 조회 성공")
	void findTopConsumptionByCategoryIdAndCurrentWeek_Success() {
		// given
		LocalDate startOfWeek = LocalDate.of(2024, 7, 1); // 월요일
		LocalDate endOfWeek = LocalDate.of(2024, 7, 7); // 일요일

		User mainUser = userRepository.save(User.builder()
			.email("email")
			.age(24)
			.name("name")
			.gender(Gender.MALE)
			.phoneNumber("010-1234-5678")
			.build());

		Category category = categoryRepository.save(Category.builder()
			.name("Test Category")
			.user(mainUser)
			.isDefault(false)
			.build());

		ConsumptionGoal goal1 = consumptionGoalRepository.save(ConsumptionGoal.builder()
			.goalAmount(5000L)
			.consumeAmount(2000L)
			.user(mainUser)
			.category(category)
			.goalMonth(startOfWeek)
			.build());

		ConsumptionGoal goal2 = consumptionGoalRepository.save(ConsumptionGoal.builder()
			.goalAmount(3000L)
			.consumeAmount(1500L)
			.user(mainUser)
			.category(category)
			.goalMonth(startOfWeek)
			.build());

		// when
		Optional<ConsumptionGoal> result = consumptionGoalRepository.findTopConsumptionByCategoryIdAndCurrentWeek(
			category.getId(), startOfWeek, endOfWeek);

		// then
		ConsumptionGoal topGoal = result.get();
		assertThat(topGoal.getConsumeAmount()).isEqualTo(2000L);
		assertThat(topGoal.getGoalAmount()).isEqualTo(5000L);
		assertThat(topGoal.getCategory().getId()).isEqualTo(category.getId());
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
}