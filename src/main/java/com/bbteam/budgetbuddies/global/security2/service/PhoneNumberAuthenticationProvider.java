package com.bbteam.budgetbuddies.global.security2.service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


@Component
public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {

    @Qualifier("MyUserDetailsService")
    private final UserDetailsService userDetailsService;
    private final OtpService otpService; // Service to verify OTPs

    public PhoneNumberAuthenticationProvider(@Qualifier("MyUserDetailsService") UserDetailsService userDetailsService
            , OtpService otpService) {
        this.userDetailsService = userDetailsService;
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
        UserDetails user  = userDetailsService.loadUserByUsername(phoneNumber);

        // Load the user after OTP verification
        if(user == null) {
            return null;
        }


        // No password check, return a token with the authenticated user
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

