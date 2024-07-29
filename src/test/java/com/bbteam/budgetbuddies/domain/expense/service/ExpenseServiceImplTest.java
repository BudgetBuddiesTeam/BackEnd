package com.bbteam.budgetbuddies.domain.expense.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.expense.converter.ExpenseConverter;
import com.bbteam.budgetbuddies.domain.expense.dto.CompactExpenseResponseDto;
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

	@Test
	@DisplayName("getMonthlyExpense : 성공")
	void getMonthlyExpense_Success() {
		// given
		final int pageSize = 5;

		User user = Mockito.spy(User.builder().build());
		given(user.getId()).willReturn(-1L);
		given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

		Category userCategory = Mockito.spy(Category.builder().build());

		LocalDate requestMonth = LocalDate.of(2024, 07, 8);
		Pageable requestPage = PageRequest.of(0, pageSize);

		List<Expense> expenses = generateExpenseList(requestMonth, user, userCategory, pageSize);

		Slice<Expense> expenseSlice = new SliceImpl<>(expenses, requestPage, false);
		given(expenseRepository.findAllByUserIdForPeriod(any(Pageable.class), any(User.class), any(LocalDateTime.class),
			any(LocalDateTime.class))).willReturn(expenseSlice);

		MonthlyExpenseCompactResponseDto expected = generateExpectation(requestMonth, pageSize);

		// when
		MonthlyExpenseCompactResponseDto result = expenseService.getMonthlyExpense(requestPage, user.getId(),
			requestMonth);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(expected);
	}

	private List<Expense> generateExpenseList(LocalDate month, User user, Category userCategory, int repeat) {
		List<Expense> expenses = new ArrayList<>();
		for (int i = repeat; i > 0; i--) {
			Expense expense = Mockito.spy(Expense.builder()
				.amount(i * 100000L)
				.description("User 소비" + i)
				.expenseDate(month.withDayOfMonth(i).atStartOfDay())
				.user(user)
				.category(userCategory)
				.build());
			given(expense.getId()).willReturn((long)-i);

			expenses.add(expense);
		}
		return expenses;
	}

	private MonthlyExpenseCompactResponseDto generateExpectation(LocalDate month, int count) {
		return MonthlyExpenseCompactResponseDto.builder()
			.expenseMonth(month.withDayOfMonth(1))
			.hasNext(false)
			.currentPage(0)
			.expenseList(generateCompactExpenseResponseList(month, count))
			.build();
	}

	private List<CompactExpenseResponseDto> generateCompactExpenseResponseList(LocalDate month, int count) {
		List<CompactExpenseResponseDto> compactExpenses = new ArrayList<>();
		for (int i = count; i > 0; i--) {
			compactExpenses.add(CompactExpenseResponseDto.builder()
				.description("User 소비" + i)
				.expenseId((long)-i)
				.expenseDate(month.withDayOfMonth(i).atStartOfDay())
				.amount(i * 100000L)
				.build());
		}
		return compactExpenses;
	}
}