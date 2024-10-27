package com.bbteam.budgetbuddies.domain.expense.converter;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.CompactExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.DailyExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.DetailExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseResponseDto;
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

	public DetailExpenseResponseDto toDetailExpenseResponseDto(Expense expense) {
		return DetailExpenseResponseDto.builder()
			.expenseId(expense.getId())
			.categoryId(expense.getCategory().getId())
			.categoryName(expense.getCategory().getName())
			.amount(expense.getAmount())
			.description(expense.getDescription())
			.expenseDate(expense.getExpenseDate())
			.build();
	}

	public MonthlyExpenseResponseDto toMonthlyExpenseResponseDto(List<Expense> expenseList, LocalDate startOfMonth) {
		Long totalConsumptionAmount = expenseList.stream().mapToLong(Expense::getAmount).sum();

		List<DailyExpenseResponseDto> dailyExpenses = toDailyExpenseResponseDto(expenseList);

		return MonthlyExpenseResponseDto.builder()
			.expenseMonth(startOfMonth)
			.totalConsumptionAmount(totalConsumptionAmount)
			.dailyExpenses(dailyExpenses)
			.build();
	}

	private List<DailyExpenseResponseDto> toDailyExpenseResponseDto(List<Expense> expenseList) {
		Map<LocalDate, List<CompactExpenseResponseDto>> expenses = expenseList.stream()
			.collect(Collectors.groupingBy(e -> e.getExpenseDate().toLocalDate(),
				Collectors.mapping(this::toExpenseCompactResponseDto, Collectors.toList())));

		return expenses.keySet().stream().map(k -> this.generateDailyExpenseResponseDto(k, expenses.get(k))).toList();
	}

	private DailyExpenseResponseDto generateDailyExpenseResponseDto(LocalDate date,
		List<CompactExpenseResponseDto> expenses) {
		return DailyExpenseResponseDto.builder()
			.daysOfMonth(date.getDayOfMonth())
			.daysOfTheWeek(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN))
			.expenses(expenses)
			.build();
	}

	private CompactExpenseResponseDto toExpenseCompactResponseDto(Expense expense) {
		return CompactExpenseResponseDto.builder()
			.expenseId(expense.getId())
			.description(expense.getDescription())
			.amount(expense.getAmount())
			.categoryId(expense.getCategory().getId())
			.build();
	}
}

