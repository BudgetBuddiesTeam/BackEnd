package com.bbteam.budgetbuddies.domain.user.service;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;

import java.util.List;

public interface UserService {
    List<UserConsumptionGoalResponse> createConsumptionGoalWithDefaultGoals(Long userId);
}
