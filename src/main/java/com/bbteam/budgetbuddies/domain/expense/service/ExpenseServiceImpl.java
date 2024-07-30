package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.expense.converter.ExpenseConverter;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final ExpenseConverter expenseConverter;

	@Override
	public ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto) {
		User user = userRepository.findById(expenseRequestDto.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

		Expense expense = expenseConverter.toExpenseEntity(expenseRequestDto, user, category);
		expenseRepository.save(expense);

		return expenseConverter.toExpenseResponseDto(expense);
	}

	@Override
	@Transactional(readOnly = true)
	public MonthlyExpenseCompactResponseDto getMonthlyExpense(Pageable pageable, Long userId, LocalDate localDate) {
		LocalDate startOfMonth = localDate.withDayOfMonth(1);
		LocalDate endOfMonth = localDate.withDayOfMonth(startOfMonth.lengthOfMonth());

		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		Slice<Expense> expenseSlice = expenseRepository.findAllByUserIdForPeriod(pageable, user,
			startOfMonth.atStartOfDay(), endOfMonth.atStartOfDay());

		return expenseConverter.toMonthlyExpenseCompactResponseDto(expenseSlice, startOfMonth);
	}

	@Override
	public ExpenseResponseDto findExpenseResponseFromUserIdAndExpenseId(Long userId, Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new IllegalArgumentException("Not found expense"));

		if (isAllowedUser(userId, expense))
			throw new IllegalArgumentException("Unauthorized user");

		return expenseConverter.toExpenseResponseDto(expense);
	}

	private boolean isAllowedUser(Long userId, Expense expense) {
		return expense.getUser().getId() != userId;
	}
}
