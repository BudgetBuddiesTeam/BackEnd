package com.bbteam.budgetbuddies.domain.supportinfo.service;

import com.bbteam.budgetbuddies.domain.supportinfo.converter.SupportInfoConverter;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfolike.entity.SupportInfoLike;
import com.bbteam.budgetbuddies.domain.supportinfolike.repository.SupportInfoLikeRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupportInfoServiceImpl implements SupportInfoService {

    private final SupportInfoRepository supportInfoRepository;

    private final SupportInfoLikeRepository supportInfoLikeRepository;

    private final SupportInfoConverter supportInfoConverter;

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public Page<SupportResponseDto> getSupportsByYearAndMonth(Integer year, Integer month, Integer page, Integer size) {
        /**
         * 1. Pageable 객체 생성 (사용자로부터 입력받은 page 번호와 size)
         * 2. 사용자가 요청한 년월을 기준으로 해당 년월의 1일로 LocalDate 객체 생성
         * 3. 해당 년월에 겹치는 할인정보 데이터 가져오기
         * 4. Entity 리스트를 -> Dto로 모두 변환하여 리턴
         */
        Pageable pageable = PageRequest.of(page, size);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Page<SupportInfo> supportInfoPage = supportInfoRepository.findByDateRange(startDate, endDate, pageable);

        return supportInfoPage.map(supportInfoConverter::toDto);
    }

    @Transactional
    @Override
    public SupportResponseDto registerSupportInfo(SupportRequest.RegisterSupportDto supportRequest) {
        /**
         * 1. RequestDto -> Entity로 변환
         * 2. Entity 저장
         * 3. Entity -> ResponseDto로 변환 후 리턴
         */
        SupportInfo entity = supportInfoConverter.toEntity(supportRequest);

        supportInfoRepository.save(entity);

        return supportInfoConverter.toDto(entity);
    }

    @Transactional
    @Override
    public SupportResponseDto toggleLike(Long userId, Long supportInfoId) {
        /**
         * 1. 사용자 조회 -> 없으면 에러
         * 2. 할인정보 조회 -> 없으면 에러
         * 3. 사용자가 특정 할인정보에 좋아요를 눌렀는지 확인 (SupportInfoLike 테이블에서 userId로부터 데이터 가져오기)
         * 4. 누르지 않은 상태라면,
         *      4-1. SupportInfo의 likeCount 1 증가
         *      4-2. SupportInfoLike의
         * 5. 이미 누른 상태라면,
         *      5-1. SupportInfo의 likeCount 1 감소
         */

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SupportInfo supportInfo = supportInfoRepository.findById(supportInfoId)
            .orElseThrow(() -> new IllegalArgumentException("SupportInfo not found"));

        Optional<SupportInfoLike> existingLike = supportInfoLikeRepository.findByUserAndSupportInfo(user, supportInfo);

        if (existingLike.isPresent()) {
            // 이미 객체가 존재한다면
            if (existingLike.get().getIsLike()) {
                // 좋아요를 누른 상태라면
                existingLike.get().toggleLike();
                supportInfo.subLikeCount();
            } else {
                // 좋아요를 누르지 않은 상태라면
                existingLike.get().toggleLike();
                supportInfo.addLikeCount();
            }
        } else {
            // 아직 좋아요를 누르지 않은 상태라면
            SupportInfoLike newLike = SupportInfoLike.builder()
                .user(user)
                .supportInfo(supportInfo)
                .isLike(true)
                .build();
            supportInfoLikeRepository.save(newLike);
            supportInfo.addLikeCount();
        }

        SupportInfo savedEntity = supportInfoRepository.save(supportInfo);

        return supportInfoConverter.toDto(savedEntity);
    }

    @Transactional
    @Override
    public SupportResponseDto updateSupportInfo(Long userId, SupportRequest.UpdateSupportDto supportRequestDto) {
        /**
         * 1. 사용자 조회 -> 없으면 에러
         * 2. 지원정보 조회 -> 없으면 에러
         * 3. 변경사항 업데이트
         * 4. 변경사항 저장
         * 5. Entity -> ResponseDto로 변환 후 리턴
         */

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SupportInfo supportInfo = supportInfoRepository.findById(supportRequestDto.getId())
            .orElseThrow(() -> new IllegalArgumentException("SupportInfo not found"));

        supportInfo.update(supportRequestDto); // 변경사항 업데이트

        supportInfoRepository.save(supportInfo); // 변경사항 저장

        return supportInfoConverter.toDto(supportInfo);
    }

    @Transactional
    @Override
    public String deleteSupportInfo(Long userId, Long supportInfoId) {
        /**
         * 1. 사용자 조회 -> 없으면 에러
         * 2. 지원정보 조회 -> 없으면 에러
         * 3. Entity 삭제
         * 4. 성공여부 반환
         */

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SupportInfo supportInfo = supportInfoRepository.findById(supportInfoId)
            .orElseThrow(() -> new IllegalArgumentException("SupportInfo not found"));

        supportInfoRepository.deleteById(supportInfoId);

        return "Success";

    }

    @Transactional
    @Override
    public SupportResponseDto getSupportInfoById(Long userId, Long supportInfoId) {
        /**
         * 1. 사용자 조회 -> 없으면 에러
         * 2. 지원정보 조회 -> 없으면 에러
         * 3. Entity 조회
         * 4. Entity -> ResponseDto로 변환 후 리턴
         */

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SupportInfo supportInfo = supportInfoRepository.findById(supportInfoId)
            .orElseThrow(() -> new IllegalArgumentException("SupportInfo not found"));

        return supportInfoConverter.toDto(supportInfo);
    }
}
