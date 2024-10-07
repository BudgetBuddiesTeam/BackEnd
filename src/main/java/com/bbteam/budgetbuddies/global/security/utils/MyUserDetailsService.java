package com.bbteam.budgetbuddies.global.security.utils;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Qualifier("MyUserDetailsService")
@RequiredArgsConstructor
public class MyUserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 조회하기 위한 레포지토리

    /**
     * 전화번호를 통해 사용자 정보를 조회하는 메서드.
     *
     * @param phoneNumber 사용자 전화번호
     * @return 조회된 사용자 정보 (User 객체)
     */
    public User loadUserByPhoneNumber(String phoneNumber) {
        // 전화번호로 사용자 정보를 조회하고 없을 시 예외 발생
        User user = userRepository.findFirstByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        return user; // 조회된 사용자 반환
    }

    /**
     * 사용자 ID를 통해 사용자 정보를 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 조회된 사용자 정보 (User 객체)
     */
    public User loadUserByUserId(Long userId) {
        // 사용자 ID로 사용자 정보를 조회하고 없을 시 예외 발생
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        return user; // 조회된 사용자 반환
    }
}