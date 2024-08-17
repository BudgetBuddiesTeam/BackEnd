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
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Slf4j
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

	public void updateConsumeAmount(Long amount) {
		this.consumeAmount += amount;
	}

	public void updateGoalAmount(Long goalAmount) {
		this.goalAmount = goalAmount;
	}

	public void restoreConsumeAmount(Long previousAmount) {
		this.consumeAmount -= previousAmount;
	}

	public void decreaseConsumeAmount(Long amount) {
		if (this.consumeAmount - amount < 0) {
			log.warn("소비 내역 삭제의 결과가 음수이므로 확인이 필요합니다.(현재 소비 금액: {}, 차감할 금액: {}). 소비 금액을 0으로 설정합니다.", this.consumeAmount, amount);
		}
		this.consumeAmount = Math.max(0, this.consumeAmount - amount);
	}
}
