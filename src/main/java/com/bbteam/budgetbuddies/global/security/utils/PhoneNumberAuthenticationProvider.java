package com.bbteam.budgetbuddies.global.security.utils;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security.otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneNumberAuthenticationProvider implements AuthenticationProvider {

    private final MyUserDetailsService myUserDetailsService; // 사용자 정보를 로드하기 위한 서비스

    private final OtpService otpService;   // OTP를 검증하기 위한 서비스

    /**
     * 전화번호와 OTP를 이용해 인증을 처리하는 메서드.
     *
     * @param authentication 인증 객체 (전화번호와 OTP 정보 포함)
     * @return 인증 성공 시 UsernamePasswordAuthenticationToken 반환
     * @throws AuthenticationException 인증 실패 시 발생하는 예외
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getName(); // 전화번호 (사용자 이름에 해당)
        String otp = authentication.getCredentials().toString(); // OTP (비밀번호에 해당)

        // OTP 검증 (OTP가 유효하지 않으면 예외 발생)
        if (!otpService.validateOtp(phoneNumber, otp)) {
            throw new GeneralException(ErrorStatus._OTP_NOT_VALID); // OTP가 유효하지 않음
        }

        // OTP 검증이 완료되면 사용자 정보 로드
        User user = myUserDetailsService.loadUserByPhoneNumber(phoneNumber);

        // 비밀번호 검증 없이 인증된 사용자 정보로 토큰 생성
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    /**
     * 해당 인증 제공자가 지원하는 인증 타입을 명시하는 메서드.
     *
     * @param authentication 지원 여부를 확인할 인증 타입 클래스
     * @return UsernamePasswordAuthenticationToken 타입을 지원하는지 여부
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication); // UsernamePasswordAuthenticationToken 지원
    }
}
