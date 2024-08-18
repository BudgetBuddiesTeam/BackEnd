package com.bbteam.budgetbuddies.domain.category.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.converter.CategoryConverter;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDTO;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDTO;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.entity.ConsumptionGoal;
import com.bbteam.budgetbuddies.domain.consumptiongoal.repository.ConsumptionGoalRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.entity.Expense;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final ConsumptionGoalService consumptionGoalService;
	private final UserRepository userRepository;
	private final CategoryConverter categoryConverter;
	private final ConsumptionGoalRepository consumptionGoalRepository;
	private final ExpenseRepository expenseRepository;

	@Override
	public CategoryResponseDTO createCategory(Long userId, CategoryRequestDTO categoryRequestDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("cannot find user"));

		if (categoryRepository.existsByUserIdAndName(userId, categoryRequestDTO.getName())) {
			throw new IllegalArgumentException("User already has a category with the same name");
		}

		Category category = categoryConverter.toCategoryEntity(categoryRequestDTO, user);
		Category savedCategory = categoryRepository.save(category);

		// custom 카테고리 생성 -> 소비 목표 테이블에 초기 값 추가
		ConsumptionGoal consumptionGoal = ConsumptionGoal.builder()
				.user(user)
				.category(savedCategory)
				.goalMonth(LocalDate.now().withDayOfMonth(1)) // custom 카테고리를 생성한 현재 달(지금)로 설정
				.goalAmount(0L)
				.consumeAmount(0L)
				.build();

		consumptionGoalRepository.save(consumptionGoal);
		return categoryConverter.toCategoryResponseDTO(savedCategory);
	}

	@Override
	public List<CategoryResponseDTO> getUserCategories(Long userId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		List<Category> categories = categoryRepository.findUserCategoryByUserId(userId);
		return categories.stream().map(categoryConverter::toCategoryResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Category handleCategoryChange(Expense expense, ExpenseUpdateRequestDto request, User user) {
		Category categoryToReplace = categoryRepository.findById(request.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Not found category"));

		consumptionGoalService.recalculateConsumptionAmount(expense, request, user);

		return categoryToReplace;
	}

	@Override
	@Transactional
	public void deleteCategory(Long categoryId, Long userId) {
		categoryRepository.findById(categoryId).ifPresent(category -> {
			if (category.getIsDefault()) {
				throw new IllegalArgumentException("Default categories cannot be deleted.");
			}

			List<Expense> expenses = expenseRepository.findByCategoryIdAndUserId(categoryId, userId);
			long totalAmount = expenses.stream().mapToLong(Expense::getAmount).sum();

			Category etcCategory = categoryRepository.findById(10L)
					.orElseThrow(() -> new IllegalArgumentException("etc category not found"));

			expenses.forEach(expense -> {
				expense.setCategory(etcCategory);
				expenseRepository.save(expense);
			});

			ConsumptionGoal goal = consumptionGoalRepository.findByCategoryIdAndUserId(10L, userId)
					.orElseThrow(() -> new IllegalArgumentException("No consumption goal found for category_id 10."));
			goal.setConsumeAmount(goal.getConsumeAmount() + totalAmount);
			consumptionGoalRepository.save(goal);

			consumptionGoalRepository.findByCategoryIdAndUserId(categoryId, userId).ifPresent(consumptionGoal -> {
				consumptionGoal.setDeleted(true);
				consumptionGoalRepository.save(consumptionGoal);
			});

			category.setDeleted(true);
			categoryRepository.save(category);
		});
	}
}