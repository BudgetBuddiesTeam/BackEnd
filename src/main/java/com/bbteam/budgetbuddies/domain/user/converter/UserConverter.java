package com.bbteam.budgetbuddies.domain.user.converter;

import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public class UserConverter {

    public static UserDto.ResponseUserDto toDto(User user) {
        return UserDto.ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .lastLoginAt(user.getLastLoginAt())
                .gender(user.getGender())
//                .consumptionPattern(user.getConsumptionPattern())
//                .photoUrl(user.getPhotoUrl())
                .email(user.getEmail())
                .age(user.getAge())
                .build();
    }

    public static User toUser(UserDto.RegisterUserDto dto) {
        return User.builder()
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .age(dto.getAge())
                .name(dto.getName())
//                .consumptionPattern(dto.getConsumptionPattern())
                .gender(dto.getGender())
//                .photoUrl(dto.getPhotoUrl())
                .build();
    }
}
