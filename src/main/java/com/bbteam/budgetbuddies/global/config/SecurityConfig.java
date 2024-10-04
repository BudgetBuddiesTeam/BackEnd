package com.bbteam.budgetbuddies.global.config;

import com.bbteam.budgetbuddies.global.security2.JwtRequestFilter;
import com.bbteam.budgetbuddies.global.security2.JwtUtil;
import com.bbteam.budgetbuddies.global.security2.MyUserDetailsService;
import com.bbteam.budgetbuddies.global.security2.service.OtpService;
import com.bbteam.budgetbuddies.global.security2.service.PhoneNumberAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Objects;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    private final Environment env;

    @Autowired
    private PhoneNumberAuthenticationProvider phoneNumberAuthenticationProvider;




    public SecurityConfig(Environment env) {
        this.env = env;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // csrf 설정 비활성화
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // session 유지 -> jwt 쓰는 의미 없음, session 뺌 -> 로그인 페이지 안됨...
                // jwt token을 받을 수 있는 페이지를 만들고, 이를 통해 로그인 한 후, swagger 는 permit으로 해서 사용해야할듯 합니다...
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                        .requestMatchers("/auth/**", "/security2/**").permitAll()
                        .anyRequest().authenticated()

            )
//
            .formLogin(withDefaults())
            .httpBasic(withDefaults())
                .authenticationProvider(phoneNumberAuthenticationProvider)
                .authenticationProvider(swaggerFormLoginAuthenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        String username = env.getProperty("spring.security.user.name");
        String password = env.getProperty("spring.security.user.password");

        log.info("username : {}", username);
        log.info("password : {}", password);


        UserDetails user = User
            .withUsername(Objects.requireNonNull(username))
            .password(passwordEncoder().encode(Objects.requireNonNull(password)))
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user);
    }


    // Swagger용 Form Login AuthenticationProvider (InMemoryUserDetailsManager 사용)
    @Bean
    public DaoAuthenticationProvider swaggerFormLoginAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService()); // InMemoryUserDetailsManager 사용
        provider.setPasswordEncoder(passwordEncoder());  // 비밀번호 암호화
        return provider;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 암호화
    }



}