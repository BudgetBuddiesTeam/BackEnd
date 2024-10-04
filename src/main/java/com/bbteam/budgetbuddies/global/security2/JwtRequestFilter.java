package com.bbteam.budgetbuddies.global.security2;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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


        // Extract Bearer token from Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
            userId = Long.parseLong(jwtUtil.extractUserId(accessToken));
            phoneNumber = jwtUtil.extractPhoneNumber(accessToken); // Assuming you're extracting phone number from JWT
        }


        // 헤더에 엑세스 토큰이 담긴 요청이라면 엑세스 토큰을 검증
        // Perform authentication if phone number is present and no authentication is set in the request
        if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtUtil.validateToken(accessToken, phoneNumber)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);// Load your custom User object
            if (jwtUtil.validateToken(accessToken, userDetails.getUsername())) {
                // Create a custom Authentication object with your User
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the Authentication in the request attribute
                request.setAttribute("authenticatedUser", token);
                SecurityContextHolder.getContext().setAuthentication(token);

            }
        }
        chain.doFilter(request, response);


        // Continue the filter chain

    }
}

