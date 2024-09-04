package com.bbteam.budgetbuddies.domain.category.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bbteam.budgetbuddies.domain.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.converter.CategoryConverter;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryRequestDto;
import com.bbteam.budgetbuddies.domain.category.dto.CategoryResponseDto;
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
	@Transactional
	public CategoryResponseDto createCategory(Long userId, CategoryRequestDto categoryRequestDto) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		// 동일한 이름의 삭제된 카테고리가 존재하는지 확인
		Optional<Category> existingCategory = categoryRepository.findByNameAndUserIdAndDeletedTrue(
				categoryRequestDto.getName(), userId);

		if (existingCategory.isPresent()) {
			// 삭제된 카테고리가 존재하면 복구 (deleted = false)
			Category categoryToRestore = existingCategory.get();
			categoryToRestore.setDeleted(false); // 카테고리 복구
			categoryRepository.save(categoryToRestore);

			// 해당 카테고리의 삭제된 ConsumptionGoal도 복구
			Optional<ConsumptionGoal> existingConsumptionGoal = consumptionGoalRepository.findByUserAndCategoryAndDeletedTrue(
					user, categoryToRestore);

			if (existingConsumptionGoal.isPresent()) {
				ConsumptionGoal consumptionGoalToRestore = existingConsumptionGoal.get();
				consumptionGoalToRestore.setDeleted(false); // ConsumptionGoal 복구
				consumptionGoalToRestore.setConsumeAmount(0L); // consumeAmount 0으로 초기화
				consumptionGoalToRestore.setGoalAmount(0L); // goalAmount 0으로 초기화
				consumptionGoalRepository.save(consumptionGoalToRestore);
			} else {
				// ConsumptionGoal이 존재하지 않으면 새로 생성
				ConsumptionGoal newConsumptionGoal = ConsumptionGoal.builder()
						.user(user)
						.category(categoryToRestore)
						.goalMonth(LocalDate.now().withDayOfMonth(1)) // 현재 달로 목표 설정
						.consumeAmount(0L)
						.goalAmount(0L)
						.deleted(false) // 생성할 때 삭제 상태가 아니도록
						.build();
				consumptionGoalRepository.save(newConsumptionGoal);
			}

			return categoryConverter.toCategoryResponseDto(categoryToRestore);
		} else {
			// 새로운 카테고리 생성
			Category newCategory = categoryConverter.toCategoryEntity(categoryRequestDto, user);
			categoryRepository.save(newCategory);

			// 새로운 카테고리에 대한 ConsumptionGoal도 생성
			ConsumptionGoal newConsumptionGoal = ConsumptionGoal.builder()
					.user(user)
					.category(newCategory)
					.goalMonth(LocalDate.now().withDayOfMonth(1)) // 현재 달로 목표 설정
					.consumeAmount(0L)
					.goalAmount(0L)
					.deleted(false) // 생성할 때 삭제 상태가 아니도록
					.build();
			consumptionGoalRepository.save(newConsumptionGoal);

			return categoryConverter.toCategoryResponseDto(newCategory);
		}
	}

	@Override
	public List<CategoryResponseDto> getUserCategories(Long userId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

		List<Category> categories = categoryRepository.findUserCategoryByUserId(userId);
		return categories.stream().map(categoryConverter::toCategoryResponseDto).collect(Collectors.toList());
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