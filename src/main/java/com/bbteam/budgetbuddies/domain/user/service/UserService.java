package com.bbteam.budgetbuddies.domain.user.service;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserConsumptionGoalResponse> createConsumptionGoalWithDefaultGoals(Long userId);

    void saveFavoriteHashtags(Long userId, List<Long> hashtagIds);

    UserDto.ResponseUserDto saveUser(UserDto.RegisterUserDto dto);

    UserDto.ResponseUserDto findUser(Long userId);

    UserDto.ResponseUserDto modifyUser(Long userId, UserDto.ModifyUserDto dto);

    List<UserDto.ResponseUserDto> findAll();
}
