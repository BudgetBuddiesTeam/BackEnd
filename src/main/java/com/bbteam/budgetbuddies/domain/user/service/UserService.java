package com.bbteam.budgetbuddies.domain.user.service;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserConsumptionGoalResponse> createConsumptionGoalWithDefaultGoals(Long userId);

    UserDto.ResponseDto saveUser(UserDto.RegisterDto dto);

    UserDto.ResponseDto findUser(Long userId);

    UserDto.ResponseDto changeUser(Long userId, String email, String name);
}
