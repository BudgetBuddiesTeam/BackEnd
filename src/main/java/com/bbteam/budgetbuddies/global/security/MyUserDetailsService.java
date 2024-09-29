package com.bbteam.budgetbuddies.global.security;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이 예제에서는 고정된 사용자 이름과 비밀번호를 사용 (실제 운영에서는 데이터베이스에서 로드)

        User user = userRepository.findFirstByPhoneNumber(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));


        return new org.springframework.security.core.userdetails.User("johndoe", "password", new ArrayList<>());
    }

    public User loadUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findFirstByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        return user;
    }
}