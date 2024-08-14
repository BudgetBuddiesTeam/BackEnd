package com.bbteam.budgetbuddies.domain.user.dto;

import com.bbteam.budgetbuddies.enums.Gender;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class UserDto {

    @Getter
    @Builder
    public static class RegisterUserDto {
        @Length(min = 11, message = "전화번호 11자리를 입력해주세요.")
        private String phoneNumber;
        private String name;
        @Min(value = 1, message = "나이는 0또는 음수가 될 수 없습니다.")
        private Integer age;
        private Gender gender;
        private String email;
        private String photoUrl;
        private String consumptionPattern;
    }

    @Getter
    @Builder
    public static class ResponseUserDto {
        private Long id;
        private String phoneNumber;
        private String name;
        private String email;
        private Integer age;
        private Gender gender;
        private String photoUrl;
        private String consumptionPattern;
        private LocalDateTime lastLoginAt;
    }
}
