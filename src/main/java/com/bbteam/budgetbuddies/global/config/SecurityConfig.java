package com.bbteam.budgetbuddies.global.config;

import com.bbteam.budgetbuddies.global.security.utils.PhoneNumberAuthenticationProvider;
import com.bbteam.budgetbuddies.global.security.jwt.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtRequestFilter jwtRequestFilter;

    private final JwtExceptionFilter jwtExceptionFilter;

    private final PhoneNumberAuthenticationProvider phoneNumberAuthenticationProvider;

    private final List<String> swaggers = List.of( // Swagger 관련 URL 목록
        "/swagger-ui/**",
        "/v3/api-docs/**"
    );

    private final List<String> auth = List.of( // 인증 관련 URL 목록
        "/auth/get-otp",
        "/auth/login"
    );


    public SecurityConfig(JwtRequestFilter jwtRequestFilter, JwtExceptionFilter jwtExceptionFilter, PhoneNumberAuthenticationProvider phoneNumberAuthenticationProvider) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtExceptionFilter = jwtExceptionFilter;
        this.phoneNumberAuthenticationProvider = phoneNumberAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // csrf 설정 비활성화
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(swaggers.toArray(new String[0])).permitAll() // 스웨거 주소 허용
                    .requestMatchers(auth.toArray(new String[0])).permitAll() // 로그인 주소 허용
                    .anyRequest().authenticated() // 그 외 모든 요청에 대해 인증 요구
            )
            .httpBasic(AbstractHttpConfigurer::disable)
            .authenticationProvider(phoneNumberAuthenticationProvider)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtRequestFilter.class); // jwt 에러처리를 위한 필터등록

        return http.build();
    }

}