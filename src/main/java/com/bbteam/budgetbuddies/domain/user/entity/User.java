package com.bbteam.budgetbuddies.domain.user.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import com.bbteam.budgetbuddies.domain.user.dto.UserDto;
import com.bbteam.budgetbuddies.enums.Gender;
import com.bbteam.budgetbuddies.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Builder.Default
    private Role role = Role.USER; // 기본값 User 권한

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

    @Column(nullable = true)
    private String mobileCarrier; // 통신사

    @Column(nullable = true)
    private String region; // 거주지

    private LocalDateTime lastLoginAt;

    public void changeUserDate(String email, String name) {
        this.name = name;
        this.email = email;
    }

    public List<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
