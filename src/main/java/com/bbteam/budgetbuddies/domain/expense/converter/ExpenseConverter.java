package com.bbteam.budgetbuddies.domain.expense.converter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.CompactExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseCompactResponseDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

@Component
public class ExpenseConverter {

	public Expense toExpenseEntity(ExpenseRequestDto expenseRequestDto, User user, Category category) {
		return Expense.builder()
			.user(user)
			.category(category)
			.amount(expenseRequestDto.getAmount())
			.description(expenseRequestDto.getDescription())
			.expenseDate(expenseRequestDto.getExpenseDate())
			.build();
	}

	public ExpenseResponseDto toExpenseResponseDto(Expense expense) {
		return ExpenseResponseDto.builder()
			.expenseId(expense.getId())
			.userId(expense.getUser().getId())
			.categoryId(expense.getCategory().getId())
			.categoryName(expense.getCategory().getName())
			.amount(expense.getAmount())
			.description(expense.getDescription())
			.expenseDate(expense.getExpenseDate())
			.build();
	}

	public MonthlyExpenseCompactResponseDto toMonthlyExpenseCompactResponseDto(Slice<Expense> expenseSlice,
		LocalDate startOfMonth) {
		List<CompactExpenseResponseDto> compactResponseList = expenseSlice.getContent().stream()
			.map(this::toExpenseCompactResponseDto).toList();

		return MonthlyExpenseCompactResponseDto
			.builder()
			.expenseMonth(startOfMonth)
			.currentPage(expenseSlice.getPageable().getPageNumber())
			.hasNext(expenseSlice.hasNext())
			.expenseList(compactResponseList)
			.build();
	}

	private CompactExpenseResponseDto toExpenseCompactResponseDto(Expense expense) {
		return CompactExpenseResponseDto.builder()
			.expenseId(expense.getId())
			.description(expense.getDescription())
			.amount(expense.getAmount())
			.expenseDate(expense.getExpenseDate())
			.build();
	}
}

