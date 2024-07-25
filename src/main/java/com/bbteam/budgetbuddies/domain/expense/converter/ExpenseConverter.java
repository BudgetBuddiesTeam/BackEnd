package com.bbteam.budgetbuddies.domain.expense.converter;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.stereotype.Component;

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
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .expenseDate(expense.getExpenseDate())
                .build();
    }
}

