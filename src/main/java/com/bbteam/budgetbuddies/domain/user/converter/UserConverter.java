package com.bbteam.budgetbuddies.domain.user.converter;

import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public class UserConverter {

    public static UserDto.ResponseDto toDto(User user) {
        return UserDto.ResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }

    public static User toUser(UserDto.RegisterDto dto) {
        return User.builder()
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .age(dto.getAge())
                .name(dto.getName())
                .consumptionPattern(dto.getConsumptionPattern())
                .gender(dto.getGender())
                .photoUrl(dto.getPhotoUrl())
                .build();
    }
}