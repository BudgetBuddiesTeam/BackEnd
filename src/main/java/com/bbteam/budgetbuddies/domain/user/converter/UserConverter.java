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
                .region(user.getRegion())
                .mobileCarrier(user.getMobileCarrier())
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
                .region(dto.getRegion())
                .mobileCarrier(dto.getMobileCarrier())
                .gender(dto.getGender())
//                .consumptionPattern(dto.getConsumptionPattern())
//                .photoUrl(dto.getPhotoUrl())
                .build();
    }

    public static String ageToAgeGroup(Integer age) {
        if (age < 20) {
            return "20세미만";
        } else if (age < 23) {
            return "20-22세";
        } else if (age < 26) {
            return "23-25세";
        } else if (age < 29) {
            return "26-28세";
        } else {
            return "29세 이상";
        }
    }
}
