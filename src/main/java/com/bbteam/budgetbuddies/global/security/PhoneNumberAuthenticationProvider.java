package com.bbteam.budgetbuddies.global.security;

import com.bbteam.budgetbuddies.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService myUserDetailsService;
    private final OtpService otpService; // Service to verify OTPs

    /**
     * @param myUserDetailsService
     * @param otpService
     */
    public PhoneNumberOtpAuthenticationProvider(MyUserDetailsService myUserDetailsService, OtpService otpService) {
        this.myUserDetailsService = myUserDetailsService;
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName();
        String otp = authentication.getCredentials().toString();

        // Verify the OTP using your OTP service
        if (!otpService.verifyOtp(phoneNumber, otp)) {
            throw new BadCredentialsException("Invalid OTP");
        }

        // Load the user after OTP verification
        User user = myUserDetailsService.loadUserByPhoneNumber(phoneNumber);

        // No password check, return a token with the authenticated user
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
