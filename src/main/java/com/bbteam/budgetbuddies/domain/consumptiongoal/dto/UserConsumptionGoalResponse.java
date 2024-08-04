package com.bbteam.budgetbuddies.domain.consumptiongoal.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserConsumptionGoalResponse {
    private Long categoryId;
    private LocalDate goalMonth;
    private Long consumeAmount;
    private Long goalAmount;
}