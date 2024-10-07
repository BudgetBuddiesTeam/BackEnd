package com.bbteam.budgetbuddies.global.security.auth.service;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.apiPayload.exception.GeneralException;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationResponse;
import com.bbteam.budgetbuddies.global.security.jwt.JwtUtil;
import com.bbteam.budgetbuddies.global.security.otp.OtpNumber;
import com.bbteam.budgetbuddies.global.security.otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtil jwtUtil; // JWT 관련 유틸리티 클래스
    private final OtpService otpService; // OTP 관련 서비스 클래스
    private final UserRepository userRepository; // 사용자 정보 저장소

    /**
     * OTP를 생성하여 반환하는 메서드.
     * @param phoneNumber 전화번호
     * @return 생성된 OTP 정보
     */
    public OtpNumber generateOtp(String phoneNumber) {
        // 전화번호를 사용하여 OTP를 생성하고 반환합니다.
        return otpService.generateOtp(phoneNumber);
    }

    /**
     * 로그인 처리 메서드.
     * 전화번호와 OTP를 검증한 후, 인증이 성공하면 JWT 토큰을 발급합니다.
     *
     * @param phoneNumber 전화번호
     * @param otpNumber 인증번호
     * @return 발급된 JWT 액세스 토큰과 리프레시 토큰 정보
     */
    public AuthenticationResponse.SendTokens login(String phoneNumber, String otpNumber) {
        // 입력된 OTP가 유효한지 검증
        if (!otpService.validateOtp(phoneNumber, otpNumber)) {
            throw new GeneralException(ErrorStatus._OTP_NOT_VALID); // 유효하지 않은 OTP일 경우 예외 발생
        }

        // 전화번호로 사용자를 로드, 존재하지 않으면 새 사용자 생성
        final User user = userRepository.findFirstByPhoneNumber(phoneNumber)
            .orElseGet(() -> userRepository.save(User.builder() // 사용자 정보가 없으면 새로 생성
                .phoneNumber(phoneNumber)
                .build()
            ));

        // JWT 액세스 토큰 발급
        final String accessToken = jwtUtil.generateAccessToken(user);
        // JWT 리프레시 토큰 발급
        final String refreshToken = jwtUtil.generateRefreshToken(user);

        // 발급된 토큰 정보를 포함한 응답 객체를 반환
        return AuthenticationResponse.SendTokens.builder()
            .userId(user.getId()) // 사용자 ID
            .phoneNumber(user.getPhoneNumber()) // 전화번호
            .accessToken(accessToken) // 액세스 토큰
            .refreshToken(refreshToken) // 리프레시 토큰
            .build();
    }

    /**
     * 새로운 액세스 토큰을 재발급하는 메서드.
     *
     * @param user 사용자 정보
     * @return 발급된 새로운 액세스 토큰 정보
     */
    public AuthenticationResponse.SendAccessToken reIssueAccessToken(User user) {
        // 새로운 액세스 토큰 발급
        String newAccessToken = jwtUtil.generateAccessToken(user);

        // 발급된 새로운 토큰 정보를 포함한 응답 객체를 반환
        return AuthenticationResponse.SendAccessToken.builder()
            .userId(user.getId()) // 사용자 ID
            .phoneNumber(user.getPhoneNumber()) // 전화번호
            .accessToken(newAccessToken) // 새로운 액세스 토큰
            .build();
    }
}