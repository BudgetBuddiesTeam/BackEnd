package com.bbteam.budgetbuddies.domain.user.service;
import com.bbteam.budgetbuddies.domain.consumptiongoal.dto.UserConsumptionGoalResponse;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationRequest;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationResponse;

import java.util.List;

public interface UserService {
    List<UserConsumptionGoalResponse> createConsumptionGoalWithDefaultGoals(Long userId);

    void saveFavoriteHashtags(Long userId, List<Long> hashtagIds);

    UserDto.ResponseUserDto saveUser(UserDto.RegisterUserDto dto);

    UserDto.ResponseUserDto findUser(Long userId);

    UserDto.ResponseUserDto modifyUser(Long userId, UserDto.ModifyUserDto dto);

    List<UserDto.ResponseUserDto> findAll();

    User getUser(Long userId);

    AuthenticationResponse.StandardInfo saveStandardInfo(UserDto.AuthUserDto user, AuthenticationRequest.StandardInfo dto);

    AuthenticationResponse.AdditionalInfo saveAdditionalInfo(UserDto.AuthUserDto user, AuthenticationRequest.AdditionalInfo dto);


}
