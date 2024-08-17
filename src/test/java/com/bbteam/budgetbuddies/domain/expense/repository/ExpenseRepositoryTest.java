package com.bbteam.budgetbuddies.domain.expense.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
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
	@DisplayName("findAllByUserIdFOrPeriod 성공")
	void findAllByUserIdForPeriod_Success() {
		// given
		User user = userRepository.save(
			User.builder().email("email").age(24).name("name").phoneNumber("010-1234-5678").build());

		Category userCategory = categoryRepository.save(
			Category.builder().name("유저 카테고리").user(user).isDefault(false).build());

		LocalDate startDate = LocalDate.of(2024, 7, 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

		List<Expense> expected = setExpense(user, userCategory, startDate);

		// when
		List<Expense> result = expenseRepository.findAllByUserIdForPeriod(user,
			startDate.atStartOfDay(), endDate.atStartOfDay());

		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	private List<Expense> setExpense(User user, Category userCategory, LocalDate startDate) {
		setUnexpectedExpense(user, userCategory, startDate);

		return setExpectedExpenseOrderByDateDesc(user, userCategory, startDate);
	}

	private List<Expense> setExpectedExpenseOrderByDateDesc(User user, Category userCategory, LocalDate startDate) {
		List<Expense> expenses = new ArrayList<>();

		for (int i = startDate.lengthOfMonth(); i > startDate.lengthOfMonth() - 5; i--) {
			Expense expense = Expense.builder()
				.user(user)
				.category(userCategory)
				.amount(100000L * i)
				.expenseDate(startDate.withDayOfMonth(i).atStartOfDay())
				.build();

			expenses.add(expenseRepository.save(expense));
		}
		return expenses;
	}

	private void setUnexpectedExpense(User user, Category userCategory, LocalDate startDate) {
		for (int i = 1; i <= 5; i++) {
			Expense expense = Expense.builder()
				.user(user)
				.category(userCategory)
				.amount(100000L * i)
				.expenseDate(startDate.withMonth(8).atStartOfDay())
				.build();

			expenseRepository.save(expense);
		}
	}
}