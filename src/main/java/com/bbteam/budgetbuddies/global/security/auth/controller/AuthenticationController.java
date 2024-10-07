package com.bbteam.budgetbuddies.global.security.auth.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationRequest;
import com.bbteam.budgetbuddies.global.security.auth.dto.AuthenticationResponse;
import com.bbteam.budgetbuddies.global.security.auth.service.AuthenticationService;
import com.bbteam.budgetbuddies.global.security.otp.OtpNumber;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth") // 인증 관련 요청을 처리하는 컨트롤러
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService; // 인증 관련 서비스

    /**
     * OTP를 요청하는 엔드포인트.
     * 전화번호를 입력받아 해당 번호로 OTP를 발송하고, 발송된 OTP 정보를 반환합니다.
     *
     * @param request OTP 요청에 필요한 전화번호 정보
     * @return 성공 시 전화번호와 생성된 OTP를 포함한 ApiResponse
     */
    @PostMapping("/get-otp")
    @Override
    public ApiResponse<AuthenticationResponse.SendOtpNumber> getOtpNumber(
        @Valid @RequestBody AuthenticationRequest.ToReceiveNumber request
    ) {
        String phoneNumber = request.getPhoneNumber(); // 요청에서 전화번호 추출
        OtpNumber generatedOtp = authenticationService.generateOtp(phoneNumber); // OTP 생성
        AuthenticationResponse.SendOtpNumber response = AuthenticationResponse.SendOtpNumber.builder()
            .phoneNumber(phoneNumber) // 전화번호 설정
            .otpNumber(generatedOtp)  // 생성된 OTP 정보 설정
            .build();
        return ApiResponse.onSuccess(response); // 성공 응답 반환
    }

    /**
     * 로그인 요청을 처리하는 엔드포인트.
     * 전화번호와 OTP를 받아 로그인 처리 후, 인증 토큰을 반환합니다.
     *
     * @param request 로그인 요청에 필요한 전화번호 및 OTP 정보
     * @return 성공 시 인증 토큰 정보를 포함한 ApiResponse
     */
    @PostMapping("/login")
    @Override
    public ApiResponse<AuthenticationResponse.SendTokens> login(
        @Valid @RequestBody AuthenticationRequest.ToLogin request
    ) {
        AuthenticationResponse.SendTokens response = authenticationService.login(
            request.getPhoneNumber(),  // 전화번호 추출
            request.getOtpNumber()     // OTP 추출
        );
        return ApiResponse.onSuccess(response); // 성공 응답 반환
    }

    /**
     * 액세스 토큰 재발급 요청을 처리하는 엔드포인트.
     * 현재 인증된 사용자로부터 새로운 액세스 토큰을 발급받습니다.
     *
     * @return 성공 시 새로 발급된 액세스 토큰 정보를 포함한 ApiResponse
     */
    @GetMapping("/reissue-access-token")
    @Override
    public ApiResponse<AuthenticationResponse.SendAccessToken> reIssueAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 현재 인증 정보 가져오기
        User user = null;

        // 인증된 사용자 정보가 있는지 확인
        if (authentication != null && authentication.isAuthenticated()) {
            user = (User) authentication.getPrincipal(); // 인증된 사용자 추출
        }

        // 새로운 액세스 토큰 발급
        AuthenticationResponse.SendAccessToken response = authenticationService.reIssueAccessToken(user);
        return ApiResponse.onSuccess(response); // 성공 응답 반환
    }
}

