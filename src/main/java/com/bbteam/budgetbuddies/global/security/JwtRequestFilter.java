package com.bbteam.budgetbuddies.global.security;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String phoneNumber = null;
        String accessToken = null;
        String refreshToken = null;

        String requestURI = request.getRequestURI(); // 요청한 주소


        // Extract Bearer token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
            userId = Long.parseLong(jwtUtil.extractUserId(accessToken));
            phoneNumber = jwtUtil.extractPhoneNumber(accessToken); // Assuming you're extracting phone number from JWT
        }

        if (isRefreshTokenApiRequest(requestURI)) { // 헤더에 리프레시 토큰이 담긴 요청이라면 리프레시 토큰을 검증

        } else { // 헤더에 엑세스 토큰이 담긴 요청이라면 엑세스 토큰을 검증
            // Perform authentication if phone number is present and no authentication is set in the request
            if (userId != null && phoneNumber != null && request.getAttribute("authenticatedUser") == null) {
                User user = this.userDetailsService.loadUserByPhoneNumber(phoneNumber); // Load your custom User object
                if (jwtUtil.validateToken(accessToken, user.getId(), user.getPhoneNumber())) {
                    // Create a custom Authentication object with your User
                    CustomAuthenticationToken authentication = new CustomAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the custom Authentication in the request attribute
                    request.setAttribute("authenticatedUser", authentication);
                } else {
                    throw new GeneralException(ErrorStatus._TOKEN_NOT_VALID);
                }
            }
        }


        // Continue the filter chain
        chain.doFilter(request, response);
    }

    private boolean isRefreshTokenApiRequest(String requestURI) {
        return "/auth/reissue-access-token".equals(requestURI);

}

