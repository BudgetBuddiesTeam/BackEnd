package com.bbteam.budgetbuddies.domain.category.service;

import java.util.List;

import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDto;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public interface CategoryService {
	CategoryResponseDto createCategory(Long userId, CategoryRequestDto categoryRequestDTO);

	List<CategoryResponseDto> getUserCategories(Long userId);

	void deleteCategory(Long id, Long userId);

	Category getCategory(Long categoryId);

	List<Category> getUserCategoryList(Long userId);
}

