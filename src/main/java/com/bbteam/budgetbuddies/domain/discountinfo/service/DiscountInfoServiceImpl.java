package com.bbteam.budgetbuddies.domain.discountinfo.service;

import com.bbteam.budgetbuddies.domain.discountinfo.converter.DiscountInfoConverter;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequestDto;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.discountinfolike.repository.DiscountInfoLikeRepository;
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
public class DiscountInfoServiceImpl implements DiscountInfoService {

    private final DiscountInfoRepository discountInfoRepository;

    private final DiscountInfoLikeRepository discountInfoLikeRepository;

    private final DiscountInfoConverter discountInfoConverter;

    private final UserRepository userRepository;


    @Transactional
    @Override
    public Page<DiscountResponseDto> getDiscountsByYearAndMonth(Integer year, Integer month, Integer page, Integer size) {
        /**
         * 1. Pageable 객체 생성 (사용자로부터 입력받은 page 번호와 size)
         * 2. 사용자가 요청한 년월을 기준으로 해당 년월의 1일로 LocalDate 객체 생성
         * 3. 해당 년월에 겹치는 할인정보 데이터 가져오기
         * 4. Entity 리스트를 -> Dto로 모두 변환하여 리턴
         */
        Pageable pageable = PageRequest.of(page, size);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Page<DiscountInfo> discountInfoPage = discountInfoRepository.findByDateRange(startDate, endDate, pageable);

        return discountInfoPage.map(discountInfoConverter::toDto);
    }

    @Transactional
    @Override
    public DiscountResponseDto registerDiscountInfo(DiscountRequestDto discountRequestDto) {
        /**
         * 1. RequestDto -> Entity로 변환
         * 2. Entity 저장
         * 3. Entity -> ResponseDto로 변환 후 리턴
         */
        DiscountInfo entity = discountInfoConverter.toEntity(discountRequestDto);

        discountInfoRepository.save(entity);

        return discountInfoConverter.toDto(entity);
    }

    @Transactional
    @Override
    public DiscountResponseDto toggleLike(Long userId, Long discountInfoId) {
        /**
         * 1. 사용자 조회 -> 없으면 에러
         * 2. 할인정보 조회 -> 없으면 에러
         * 3. 사용자가 특정 할인정보에 좋아요를 눌렀는지 확인 (DiscountInfoLike 테이블에서 userId로부터 데이터 가져오기)
         * 4. 누르지 않은 상태라면,
         *      4-1. DiscountInfo의 likeCount 1 증가
         *      4-2. DiscountInfoLike의
         * 5. 이미 누른 상태라면,
         *      5-1. DiscountInfo의 likeCount 1 감소
         */

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        DiscountInfo discountInfo = discountInfoRepository.findById(discountInfoId)
            .orElseThrow(() -> new IllegalArgumentException("DiscountInfo not found"));

        Optional<DiscountInfoLike> existingLike = discountInfoLikeRepository.findByUserAndDiscountInfo(user, discountInfo);

        if (existingLike.isPresent()) {
            // 이미 좋아요를 누른 상태라면
            discountInfoLikeRepository.delete(existingLike.get());
            discountInfo.setLikeCount(discountInfo.getLikeCount() - 1);
        } else {
            // 아직 좋아요를 누르지 않은 상태라면
            DiscountInfoLike newLike = DiscountInfoLike.builder()
                .user(user)
                .discountInfo(discountInfo)
                .build();
            discountInfoLikeRepository.save(newLike);
            discountInfo.setLikeCount(discountInfo.getLikeCount() + 1);
        }

        DiscountInfo savedEntity = discountInfoRepository.save(discountInfo);

        return discountInfoConverter.toDto(savedEntity);
    }


}
