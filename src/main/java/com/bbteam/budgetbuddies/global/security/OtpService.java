package com.bbteam.budgetbuddies.global.security;

import org.springframework.stereotype.Service;

@Service
public class OtpService {


    public boolean verifyOtp(String phoneNumber, String otp) {
        return true;
    }
}
