package com.bbteam.budgetbuddies.global.security.otp;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OtpService {

    // OTP를 저장할 캐시. ConcurrentHashMap을 사용하여 다중 스레드 환경에서 안전하게 OTP를 저장
    private final Map<String, OtpNumber> otpCache = new ConcurrentHashMap<>();

    // Cool-SMS 메시지 서비스를 위한 변수
    private DefaultMessageService messageService;

    // OTP 인증번호 유효 시간 (초 단위): 3분
    private static final long OTP_VALID_DURATION = 3 * 60;

    @Value("${cool-sms.api-key}")
    private String apiKey;

    @Value("${cool-sms.api-secret}")
    private String apiSecret;

    @Value("${cool-sms.sender-phone-number}")
    private String senderPhoneNumber;

    // 초기화 메소드: Cool-SMS 메시지 서비스 인스턴스를 초기화
    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * OTP를 생성하고 캐시에 저장하는 메소드
     *
     * @param phoneNumber 사용자의 전화번호
     * @return 생성된 OTP 정보가 담긴 OtpNumber 객체
     */
    public OtpNumber generateOtp(String phoneNumber) {
        // 6자리 랜덤 OTP 번호 생성
        String randomNumber = String.valueOf((int)(Math.random() * 900000) + 100000);

        // OTP 만료 시간 설정 (현재 시간 기준으로 3분 뒤)
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(OTP_VALID_DURATION);

        // OTP 정보를 담은 객체 생성
        OtpNumber otp = OtpNumber.builder()
            .otp(randomNumber)
            .expirationTime(expirationTime)
            .build();

        // 생성된 OTP를 전화번호를 키로 하여 캐시에 저장
        otpCache.put(phoneNumber, otp);

        // 실제 메시지 전송은 주석 처리되어 있음 (실제 배포 시 주석 해제)
        // sendMessage(phoneNumber, otp);

        return otp;
    }

    /**
     * OTP 문자 메시지를 전송하는 메소드
     *
     * @param phoneNumber 수신자의 전화번호
     * @param otp         생성된 OTP 객체
     */
    public void sendMessage(String phoneNumber, OtpNumber otp) {
        Message message = new Message();

        // 발신 번호 및 수신 번호 설정 (01012345678 형태로 입력해야 함)
        message.setFrom(senderPhoneNumber); // 발신 번호 설정
        message.setTo(phoneNumber); // 수신 번호 설정

        // 메시지 내용 설정 (한글 45자 이하일 경우 자동으로 SMS로 전송)
        message.setText("[빈주머니즈]\n인증번호: " + otp.getOtp());

        // 메시지 전송 요청 및 응답 로그 출력
        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        log.info("전송된 메시지: {}", response);
    }

    /**
     * OTP가 유효한지 검증하는 메소드
     *
     * @param phoneNumber 사용자의 전화번호
     * @param otp         사용자가 입력한 OTP
     * @return OTP가 유효하고 만료되지 않았으면 true, 그렇지 않으면 false
     */
    public boolean validateOtp(String phoneNumber, String otp) {
        // 캐시에서 전화번호에 해당하는 OTP 가져오기
        OtpNumber otpEntry = otpCache.get(phoneNumber);

        if (otpEntry == null) {
            return false; // 캐시에 OTP가 없으면 false 반환
        }

        // OTP가 일치하고 유효 기간 내에 있는지 확인
        if (otpEntry.getOtp().equals(otp) && otpEntry.getExpirationTime().isAfter(LocalDateTime.now())) {
            otpCache.remove(phoneNumber); // OTP가 유효하면 사용 후 제거
            return true;
        }

        return false; // OTP가 일치하지 않거나 만료된 경우 false 반환
    }
}