package com.bbteam.budgetbuddies.global.security;

import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final User user;
    private Object credentials;

    public CustomAuthenticationToken(User user, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.credentials = credentials;
        super.setAuthenticated(true); // This can be set based on the actual authentication logic
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return user; // This returns your custom User object
    }
}
