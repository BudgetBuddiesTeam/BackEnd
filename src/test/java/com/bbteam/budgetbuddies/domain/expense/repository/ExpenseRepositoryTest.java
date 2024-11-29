package com.bbteam.budgetbuddies.domain.expense.repository;

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
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

@DisplayName("Expense 레포지토리 테스트의 ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ExpenseRepositoryTest {
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void 월별_소비조회_월초_월말_조회_성공() {
		// given
		User user = userRepository.save(User.builder()
			.email("email")
			.age(24)
			.name("test user")
			.phoneNumber("010-1234-5678")
			.mobileCarrier("TEST")
			.build());

		Category userCategory = categoryRepository.save(
			Category.builder().name("유저 카테고리").user(user).isDefault(false).build());

		LocalDate startMonth = LocalDate.of(2024, 7, 1);
		LocalDate nextMonth = startMonth.plusMonths(1L);

		List<Expense> expected = getExpectedExpense(user, userCategory);
		setUnExpectedExpense(user, userCategory, nextMonth);

		// when
		List<Expense> result = expenseRepository.findAllByUserIdForPeriod(user.getId(), startMonth.atStartOfDay(),
			nextMonth.atStartOfDay());

		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	private List<Expense> getExpectedExpense(User user, Category userCategory) {
		Expense monthOfStartExpense = Expense.builder()
			.user(user)
			.category(userCategory)
			.amount(100000L)
			.expenseDate(LocalDate.of(2024, 7, 1).atStartOfDay())
			.build();

		Expense monthOfLastExpense = Expense.builder()
			.user(user)
			.category(userCategory)
			.amount(100000L)
			.expenseDate(LocalDate.of(2024, 7, 31).atTime(23, 59))
			.build();

		return List.of(expenseRepository.save(monthOfLastExpense), expenseRepository.save(monthOfStartExpense));
	}

	private void setUnExpectedExpense(User user, Category userCategory, LocalDate nextMonth) {
		expenseRepository.save(Expense.builder()
			.user(user)
			.category(userCategory)
			.amount(100000L)
			.expenseDate(LocalDate.of(2024, 8, 1).atStartOfDay())
			.build());

		expenseRepository.save(Expense.builder()
			.user(user)
			.category(userCategory)
			.amount(100000L)
			.expenseDate(LocalDate.of(2024, 6, 30).atTime(23, 59))
			.build());
	}
}