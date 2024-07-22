package com.bbteam.budgetbuddies.domain.consumptiongoal.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

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
		List<ConsumptionGoal> result = consumptionGoalRepository.findConsumptionGoalByUserIdAndGoalMonth(mainUser.getId(),
			goalMonth);

		// then
		assertThat(result).usingRecursiveComparison()
			.isEqualTo(List.of(defaultCategoryConsumptionGoal, userCategoryConsumptionGoal));
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