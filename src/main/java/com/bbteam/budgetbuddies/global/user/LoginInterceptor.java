package com.bbteam.budgetbuddies.global.user;

import com.bbteam.budgetbuddies.global.security2.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if(authorization == null) {
            return false;
        }
        String jwt = authorization.substring(7);

        Long userId = Long.parseLong(jwtUtil.extractUserId(jwt));
        request.setAttribute("userId", userId);


        return true;
    }
}
