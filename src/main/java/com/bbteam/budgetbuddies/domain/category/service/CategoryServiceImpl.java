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

			// 현재 날짜를 기준으로 해당 월의 시작과 끝 날짜 계산
			LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
			LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

			// 현재 월에 해당하는 삭제되지 않은 Expense 조회 (deleted = false)
			List<Expense> currentMonthExpenses = expenseRepository.findByCategoryIdAndUserIdAndExpenseDateBetweenAndDeletedFalse(
					categoryId, userId, startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59));

			long totalAmount = currentMonthExpenses.stream()
					.mapToLong(Expense::getAmount)
					.sum();

			// category_id = 10(기타 카테고리)의 소비 목표 업데이트 (custom 카테고리 삭제로 인한 소비 내역은 삭제되지 않고 기타 카테고리로..)
			ConsumptionGoal goal = consumptionGoalRepository.findByCategoryIdAndUserId(10L, userId)
					.orElseThrow(() -> new IllegalArgumentException("No consumption goal found for category_id 10."));
			goal.setConsumeAmount(goal.getConsumeAmount() + totalAmount);
			consumptionGoalRepository.save(goal);

			// 삭제되지 않은 모든 기간의 Expense 조회 (deleted = false)
			List<Expense> allExpenses = expenseRepository.findByCategoryIdAndUserIdAndDeletedFalse(categoryId, userId);

			// 해당 카테고리에 부합하는 모든 기간의 Expense들을 etc 카테고리로 이동
			allExpenses.forEach(expense -> {
				expense.setCategory(goal.getCategory());
				expenseRepository.save(expense);
			});

			// 해당 카테고리의 ConsumptionGoal 삭제
			consumptionGoalRepository.softDeleteByCategoryIdAndUserId(categoryId, userId);

			// 카테고리 삭제
			category.setDeleted(true);
			categoryRepository.save(category);
		});
	}

}