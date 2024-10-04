package com.bbteam.budgetbuddies.global.security2;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
@Qualifier("MyUserDetailsService")
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 이 예제에서는 고정된 사용자 이름과 비밀번호를 사용 (실제 운영에서는 데이터베이스에서 로드)

        Optional<User> OptionalUser = userRepository.findByPhoneNumber(username);

        User user = null;

        if(OptionalUser.isEmpty()) {
            return null;
        }
        user = OptionalUser.get();

        SimpleGrantedAuthority role = null;


        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getId().toString(), Arrays.asList(user.getAuthorities().toArray(new GrantedAuthority[0])));
    }




}