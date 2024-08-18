package com.bbteam.budgetbuddies.domain.expense.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.bbteam.budgetbuddies.domain.expense.converter.ExpenseConverter;
import com.bbteam.budgetbuddies.domain.expense.dto.CompactExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpenseService 테스트의 ")
class ExpenseServiceImplTest {
	@InjectMocks
	private ExpenseServiceImpl expenseService;
	@Mock
	private ExpenseRepository expenseRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Spy
	private ExpenseConverter expenseConverter;

	private User user;

	@BeforeEach
	void setUp() {
		user = Mockito.spy(User.builder().build());
		given(user.getId()).willReturn(-1L);
	}

	@Test
	@DisplayName("월별 소비 조회 소비를 d일 N요일로 묶어서 반환")
	void getMonthlyExpense_Success() {
		// given
		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

		Category userCategory = Mockito.spy(Category.builder().build());
		given(userCategory.getId()).willReturn(-1L);

		LocalDate requestMonth = LocalDate.of(2024, 7, 8);

		List<Expense> expenses = generateExpenseList(requestMonth, user, userCategory);

		given(expenseRepository.findAllByUserIdForPeriod(any(User.class), any(LocalDateTime.class),
			any(LocalDateTime.class))).willReturn(expenses);

		MonthlyExpenseCompactResponseDto expected =
			MonthlyExpenseCompactResponseDto.builder()
				.expenseMonth(LocalDate.of(2024, 07, 01))
				.totalConsumptionAmount(300_000L)
				.expenses(Map.of(
					"2일 화요일", List.of(CompactExpenseResponseDto.builder()
						.amount(200_000L)
						.description("User 소비")
						.expenseId(-2L)
						.categoryId(userCategory.getId())
						.build()),
					"1일 월요일", List.of(CompactExpenseResponseDto.builder()
						.amount(100_000L)
						.description("User 소비")
						.expenseId(-1L)
						.categoryId(userCategory.getId())
						.build())))
				.build();

		// when
		MonthlyExpenseCompactResponseDto result = expenseService.getMonthlyExpense(user.getId(), requestMonth);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	private List<Expense> generateExpenseList(LocalDate month, User user, Category userCategory) {
		Expense e1 = Mockito.spy(Expense.builder()
			.amount(100_000L)
			.description("User 소비")
			.expenseDate(month.withDayOfMonth(1).atStartOfDay())
			.user(user)
			.category(userCategory)
			.build());
		given(e1.getId()).willReturn((-1L));

		Expense e2 = Mockito.spy(Expense.builder()
			.amount(200_000L)
			.description("User 소비")
			.expenseDate(month.withDayOfMonth(2).atStartOfDay())
			.user(user)
			.category(userCategory)
			.build());
		given(e2.getId()).willReturn((-2L));

		return List.of(e1, e2);
	}

	@Test
	@DisplayName("findExpenseFromUserIdAndExpenseId : 성공")
	void findExpenseResponseFromUserIdAndExpenseId_Success() {
		// given
		final Long expenseId = -1L;

		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").build());
		given(userCategory.getId()).willReturn(-1L);

		Expense expense = Mockito.spy(Expense.builder().user(user).category(userCategory).description("유저 소비").build());
		given(expense.getId()).willReturn(expenseId);
		given(expenseRepository.findById(expense.getId())).willReturn(Optional.of(expense));

		ExpenseResponseDto expected = ExpenseResponseDto.builder()
			.userId(user.getId())
			.expenseId(-1L)
			.description("유저 소비")
			.categoryName("유저 카테고리")
			.categoryId(-1L)
			.build();

		// when
		ExpenseResponseDto result = expenseService.findExpenseResponseFromUserIdAndExpenseId(user.getId(), expenseId);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("findExpenseFromUserIdAndExpenseId : 소비 유저와 다른 유저로 인한 예외 반환")
	void findExpenseResponseFromUserIdAndExpenseId_Fail() {
		// given
		final Long expenseId = -1L;

		Category userCategory = Mockito.spy(Category.builder().name("유저 카테고리").build());

		Expense expense = Mockito.spy(Expense.builder().user(user).category(userCategory).description("유저 소비").build());
		given(expense.getId()).willReturn(expenseId);
		given(expenseRepository.findById(expense.getId())).willReturn(Optional.of(expense));

		// then
		assertThrows(IllegalArgumentException.class,
			() -> expenseService.findExpenseResponseFromUserIdAndExpenseId(-2L, expenseId));
	}
}