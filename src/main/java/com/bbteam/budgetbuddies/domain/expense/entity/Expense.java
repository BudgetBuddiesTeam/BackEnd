package com.bbteam.budgetbuddies.domain.expense.entity;

import java.time.LocalDateTime;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
import com.bbteam.budgetbuddies.domain.expense.dto.ExpenseUpdateRequestDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class Expense extends BaseEntity {

	@Column(nullable = false)
	@Min(value = 1, message = "0 또는 음수의 비용을 지출할 수 없습니다.")
	private Long amount;

	private String description;

	private LocalDateTime expenseDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	public void updateExpenseFromRequest(ExpenseUpdateRequestDto request, Category category) {
		this.amount = request.getAmount();
		this.expenseDate = request.getExpenseDate();
		this.category = category;
	}
}
