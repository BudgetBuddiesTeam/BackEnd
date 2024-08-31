package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.category.service.CategoryService;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;
import com.bbteam.budgetbuddies.domain.expense.converter.ExpenseConverter;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseResponseDto;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.expense.dto.MonthlyExpenseResponseDto;
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
	private final CategoryService categoryService;
	private final ExpenseConverter expenseConverter;
	private final ConsumptionGoalService consumptionGoalService;

	@Override
	public ExpenseResponseDto createExpense(Long userId, ExpenseRequestDto expenseRequestDto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
		if (Boolean.TRUE.equals(category.getDeleted())) {
			throw new IllegalArgumentException("Cannot add expense to a deleted category.");
		}

        /*
        case 1)
         - 카테고리 ID가 1~10 사이 && default => DB의 immutable 필드인 default category
         - DB 관리 이슈로 category에 default 카테고리의 중복이 발생할 경우, 이를 대비하기 위해 1<= id <= 10 조건도 추가
         */
		// default category
		if (expenseRequestDto.getCategoryId() >= 1 && expenseRequestDto.getCategoryId() <= 10
			&& category.getIsDefault()) {
		}

        /*
         Case 2)
         !default && 키테고리 테이블의 UserId 컬럼의 값이 나와 맞으면 (= custom category)
         */
		// custom category
		else if (!category.getIsDefault() && category.getUser().getId().equals(userId)) {
		} else {
			throw new IllegalArgumentException("User and category are not matched properly.");
		}

		Expense expense = expenseConverter.toExpenseEntity(expenseRequestDto, user, category);
		expenseRepository.save(expense);

		// expenseDate가 현재 월인지 확인
		LocalDate expenseDateMonth = expenseRequestDto.getExpenseDate().toLocalDate().withDayOfMonth(1);
		LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

		if (expenseDateMonth.equals(currentMonth)) {
			// 현재 월의 소비 내역일 경우 ConsumptionGoal을 업데이트
			consumptionGoalService.updateConsumeAmount(userId, expenseRequestDto.getCategoryId(), expenseRequestDto.getAmount());
		}
//		else {
//			// 과거 월의 소비 내역일 경우 해당 월의 ConsumptionGoal을 업데이트 또는 삭제 상태로 생성
//			consumptionGoalService.updateOrCreateDeletedConsumptionGoal(userId, expenseRequestDto.getCategoryId(), expenseDateMonth, expenseRequestDto.getAmount());
//		}

		return expenseConverter.toExpenseResponseDto(expense);
        /*
         Case 1 결과) 해당 유저의 user_id + immutable 필드 중 하나의 조합으로 Expense 테이블에 저장
         Case 2 결과) 내가 직접 생성한 카테고리 중 하나로 카테고리를 설정하여 Expense 테이블에 저장
         */
	}

	@Override
	@Transactional
	public void deleteExpense(Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
				.orElseThrow(() -> new IllegalArgumentException("Not found Expense"));

		Long userId = expense.getUser().getId();
		Long categoryId = expense.getCategory().getId();
		Long amount = expense.getAmount();
		LocalDate expenseDate = expense.getExpenseDate().toLocalDate();
		LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

		if (expenseDate.withDayOfMonth(1).equals(currentMonth)) {
			// 현재 달에 해당하는 소비 내역인 경우, 소비 금액 차감 로직 실행
			expenseRepository.delete(expense);
			consumptionGoalService.decreaseConsumeAmount(userId, categoryId, amount, expenseDate);
		} else {
			// 과거 달의 소비 내역인 경우, soft delete 처리
			expenseRepository.softDeleteById(expenseId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public MonthlyExpenseResponseDto getMonthlyExpense(Long userId, LocalDate localDate) {
		LocalDate startOfMonth = localDate.withDayOfMonth(1);
		LocalDate endOfMonth = startOfMonth.plusMonths(1);

		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		List<Expense> expenseSlice = expenseRepository.findAllByUserIdForPeriod(user,
			startOfMonth.atStartOfDay(), endOfMonth.atStartOfDay());

		return expenseConverter.toMonthlyExpenseResponseDto(expenseSlice, startOfMonth);
	}

	@Override
	public ExpenseResponseDto findExpenseResponseFromUserIdAndExpenseId(Long userId, Long expenseId) {
		Expense expense = expenseRepository.findById(expenseId)
			.orElseThrow(() -> new IllegalArgumentException("Not found expense"));

		checkUserAuthority(userId, expense);

		return expenseConverter.toExpenseResponseDto(expense);
	}

	private void checkUserAuthority(Long userId, Expense expense) {
		if (!expense.getUser().getId().equals(userId))
			throw new IllegalArgumentException("Unauthorized user");
	}

	@Override
	@Transactional
	public ExpenseResponseDto updateExpense(Long userId, ExpenseUpdateRequestDto request) {
		Expense expense = expenseRepository.findById(request.getExpenseId())
			.orElseThrow(() -> new IllegalArgumentException("Not found expense"));

		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Not found user"));
		checkUserAuthority(userId, expense);

		Category categoryToReplace = categoryService.handleCategoryChange(expense, request, user);

		expense.updateExpenseFromRequest(request, categoryToReplace);

		return expenseConverter.toExpenseResponseDto(expenseRepository.save(expense));
	}
}

