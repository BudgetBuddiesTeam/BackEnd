package com.bbteam.budgetbuddies.domain.user.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String name;

    @Min(value = 1, message = "나이는 0또는 음수가 될 수 없습니다.")
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20)")
    private Gender gender;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(length = 1000)
    private String photoUrl;

    private String consumptionPattern;

    private LocalDateTime lastLoginAt;

    public void changeUserDate(String email, String name) {
        this.name = name;
        this.email = email;
    }

}
