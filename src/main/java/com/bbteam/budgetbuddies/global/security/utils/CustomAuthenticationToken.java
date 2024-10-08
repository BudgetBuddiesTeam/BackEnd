package com.bbteam.budgetbuddies.global.security.utils;

import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final User user; // 사용자 정보가 담긴 커스텀 User 객체
    private Object credentials; // 사용자 인증 자격 정보 (비밀번호 또는 토큰 등)

    /**
     * CustomAuthenticationToken 생성자.
     *
     * @param user 커스텀 User 객체
     * @param credentials 인증 자격 정보 (예: 비밀번호 또는 토큰)
     * @param authorities GrantedAuthority 컬렉션 (사용자의 권한 정보)
     */
    public CustomAuthenticationToken(User user, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities); // 부모 클래스에 권한 정보 전달
        this.user = user; // 사용자 정보 설정
        this.credentials = credentials; // 인증 자격 정보 설정
        super.setAuthenticated(true); // 인증 완료 상태로 설정 (인증된 상태로 처리)
    }

    /**
     * 사용자 인증 자격 정보를 반환하는 메서드.
     *
     * @return 인증 자격 정보 (비밀번호 또는 토큰 등)
     */
    @Override
    public Object getCredentials() {
        return credentials;
    }

    /**
     * 인증된 사용자 정보를 반환하는 메서드.
     *
     * @return 커스텀 User 객체 (사용자 정보)
     */
    @Override
    public User getPrincipal() {
        return user; // 사용자 정보를 담고 있는 커스텀 User 객체 반환
    }
}
