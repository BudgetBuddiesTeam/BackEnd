package com.bbteam.budgetbuddies.domain.category.service;

import java.util.List;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public interface CategoryService {
	CategoryResponseDTO createCategory(Long userId, CategoryRequestDTO categoryRequestDTO);

	List<CategoryResponseDTO> getUserCategories(Long userId);

	Category handleCategoryChange(Expense expense, ExpenseUpdateRequestDto request, User user);
}

