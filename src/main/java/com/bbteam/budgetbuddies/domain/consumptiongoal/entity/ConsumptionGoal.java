package com.bbteam.budgetbuddies.domain.consumptiongoal.entity;

import java.time.LocalDate;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.category.entity.Category;
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
public class ConsumptionGoal extends BaseEntity {

	@Column(nullable = false)
	@Min(value = 0, message = "음수의 목표금액을 설정할 수 없습니다.")
	private Long goalAmount;

	@Column(nullable = false)
	@Min(value = 0, message = "음수의 소비금액을 설정할 수 없습니다.")
	private Long consumeAmount;

	@Column(nullable = false)
	private LocalDate goalMonth;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	public void updateGoalAmount(Long goalAmount) {
		this.goalAmount = goalAmount;
	}
}
