package com.bbteam.budgetbuddies.domain.user.repository;

import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;



    @Test
    @DisplayName("테스트용 User Entity 1개 생성")
    void saveUser() {
        User user = User.builder()
            .id(1L)
            .phoneNumber("010-1234-1234")
            .name("홍길동")
            .age(25)
            .gender(Gender.MALE)
            .email("hong@naver.com")
//            .photoUrl("abc")
//            .consumptionPattern("TypeA")
            .lastLoginAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
    }

}