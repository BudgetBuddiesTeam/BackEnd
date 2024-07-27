package com.bbteam.budgetbuddies.domain.expense.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.category.repository.CategoryRepository;
import com.bbteam.budgetbuddies.domain.consumptiongoal.service.ConsumptionGoalService;
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
	private final ConsumptionGoalService consumptionGoalService;

	@Override
	public ExpenseResponseDto createExpense(ExpenseRequestDto expenseRequestDto) {
		User user = userRepository.findById(expenseRequestDto.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
		Category category = categoryRepository.findById(expenseRequestDto.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        /*
        case 1)
         - 카테고리 ID가 1~10 사이 && default => DB의 immutable 필드인 default category
         - DB 관리 이슈로 category에 default 카테고리의 중복이 발생할 경우, 이를 대비하기 위해 1<= id <= 10 조건도 추가
         */
		if (expenseRequestDto.getCategoryId() >= 1 && expenseRequestDto.getCategoryId() <= 10 && category.getIsDefault()) {
			//  category.setUser(user);
			// default category
		}
        /*
         Case 2)
         !default && 키테고리 테이블의 UserId 컬럼의 값이 나와 맞으면 (= custom cateogory)
         */
		else if (!category.getIsDefault() && category.getUser().getId().equals(expenseRequestDto.getUserId())) {
			// custom category
		}
		else {
			throw new IllegalArgumentException("User and category are not matched properly.");
		}

		Expense expense = expenseConverter.toExpenseEntity(expenseRequestDto, user, category);
		expenseRepository.save(expense);

		// 소비 목표 업데이트
		consumptionGoalService.updateConsumeAmount(expenseRequestDto.getUserId(), expenseRequestDto.getCategoryId(), expenseRequestDto.getAmount());

		return expenseConverter.toExpenseResponseDto(expense);
        /*
         결과 Case 1) 해당 유저의 user_id + immutable 필드 중 하나의 조합으로 Expense 테이블에 저장
         결과 Case 2) 내가 직접 생성한 카테고리 중 하나로 카테고리를 설정하여 Expense 테이블에 저장
         */
	}

	@Override
	@Transactional(readOnly = true)
	public MonthlyExpenseCompactResponseDto getMonthlyExpense(Pageable pageable, Long userId, LocalDate localDate) {
		LocalDate startOfMonth = localDate.withDayOfMonth(1);
		LocalDate endOfMonth = localDate.withDayOfMonth(startOfMonth.lengthOfMonth());

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

		Slice<Expense> expenseSlice = expenseRepository.findAllByUserIdForPeriod(pageable, user,
				startOfMonth.atStartOfDay(), endOfMonth.atStartOfDay());

		return expenseConverter.toMonthlyExpenseCompactResponseDto(expenseSlice, startOfMonth);
	}
}

