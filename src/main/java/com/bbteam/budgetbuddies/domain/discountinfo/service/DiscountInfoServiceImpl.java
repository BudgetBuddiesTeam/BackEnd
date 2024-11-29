package com.bbteam.budgetbuddies.domain.discountinfo.service;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.converter.DiscountInfoConverter;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.discountinfolike.repository.DiscountInfoLikeRepository;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.hashtag.repository.HashtagRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountInfoServiceImpl implements DiscountInfoService {

    private final DiscountInfoRepository discountInfoRepository;

    private final DiscountInfoLikeRepository discountInfoLikeRepository;

    private final DiscountInfoConverter discountInfoConverter;

    private final UserRepository userRepository;

    private final HashtagRepository hashtagRepository;

    private final ConnectedInfoRepository connectedInfoRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<DiscountResponseDto> getDiscountsByYearAndMonth(Integer year, Integer month, Integer page, Integer size) {
        /**
         * 1. Pageable 객체 생성: 사용자가 요청한 페이지 번호와 사이즈를 기반으로 Pageable 객체를 생성합니다.
         * 2. 사용자가 요청한 년월에 해당하는 LocalDate 객체 생성: 해당 월의 첫날과 마지막 날을 기준으로 LocalDate 객체를 생성합니다.
         * 3. 해당 년월에 겹치는 할인정보 데이터 가져오기: Repository를 사용하여 해당 월에 포함되는 할인정보를 페이지네이션하여 가져옵니다.
         * 4. DiscountInfo 엔티티를 통해 ConnectedInfo 리스트 가져오기: 각 DiscountInfo에 대해 연결된 ConnectedInfo 리스트를 가져옵니다.
         * 5. DiscountInfo와 ConnectedInfo 리스트를 Dto로 변환하여 리턴: 모든 데이터를 DTO로 변환하여 반환합니다.
         */
        Pageable pageable = PageRequest.of(page, size);

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Page<DiscountInfo> discountInfoPage = discountInfoRepository.findByDateRange(startDate, endDate, pageable);

        return discountInfoPage.map(
            discountInfo -> {
                List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);
                return discountInfoConverter.toDto(discountInfo, connectedInfos);
            }
        );
    }

    @Transactional
    @Override
    public DiscountResponseDto registerDiscountInfo(DiscountRequest.RegisterDiscountDto discountRequestDto) {
        /**
         * 1. RequestDto를 Entity로 변환: 입력받은 DTO를 DiscountInfo 엔티티로 변환합니다.
         * 2. Entity 저장: 변환된 엔티티를 Repository를 통해 데이터베이스에 저장합니다.
         * 3. Hashtag 엔티티 조회 및 연결: 입력받은 해시태그 ID 리스트를 사용하여 Hashtag 엔티티를 조회하고, 각 해시태그와 DiscountInfo를 연결하는 ConnectedInfo 엔티티를 생성하여 저장합니다.
         * 4. ConnectedInfo 리스트 조회: 저장된 DiscountInfo와 관련된 ConnectedInfo 리스트를 조회합니다.
         * 5. Entity를 ResponseDto로 변환하여 반환: 저장된 DiscountInfo와 관련된 ConnectedInfo 리스트를 포함한 DTO를 반환합니다.
         */
        DiscountInfo entity = discountInfoConverter.toEntity(discountRequestDto);

        discountInfoRepository.save(entity);

        List<Hashtag> hashtags = hashtagRepository.findByIdIn(discountRequestDto.getHashtagIds());
        hashtags.forEach(hashtag -> {
            ConnectedInfo connectedInfo = ConnectedInfo.builder()
                .discountInfo(entity)
                .hashtag(hashtag)
                .build();
            connectedInfoRepository.save(connectedInfo);
        });

        List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(entity);
        return discountInfoConverter.toDto(entity, connectedInfos);
    }

    @Transactional
    @Override
    public DiscountResponseDto toggleLike(Long userId, Long discountInfoId) {
        /**
         * 1. 사용자 조회: 주어진 userId로 User 엔티티를 조회하며, 존재하지 않을 경우 에러를 발생시킵니다.
         * 2. 할인정보 조회: 주어진 discountInfoId로 DiscountInfo 엔티티를 조회하며, 존재하지 않을 경우 에러를 발생시킵니다.
         * 3. 사용자가 특정 할인정보에 좋아요를 눌렀는지 확인: DiscountInfoLike 테이블을 통해 사용자가 해당 할인정보에 좋아요를 눌렀는지 확인합니다.
         * 4. 좋아요 상태에 따라 처리:
         *    - 이미 좋아요를 누른 경우: 좋아요 상태를 변경하고, DiscountInfo의 likeCount를 감소시킵니다.
         *    - 좋아요를 누르지 않은 경우: 좋아요 상태를 변경하고, DiscountInfo의 likeCount를 증가시킵니다.
         * 5. 새 좋아요 객체 생성: 좋아요가 처음 생성된 경우 새로운 DiscountInfoLike 객체를 생성하고 저장합니다.
         * 6. 저장된 DiscountInfo와 관련된 ConnectedInfo 리스트 조회: 업데이트된 DiscountInfo와 연결된 ConnectedInfo 리스트를 조회합니다.
         * 7. Entity를 ResponseDto로 변환하여 반환: 최종적으로 업데이트된 DiscountInfo와 관련된 ConnectedInfo 리스트를 포함한 DTO를 반환합니다.
         */
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        DiscountInfo discountInfo = discountInfoRepository.findById(discountInfoId)
            .orElseThrow(() -> new IllegalArgumentException("DiscountInfo not found"));

        Optional<DiscountInfoLike> existingLike = discountInfoLikeRepository.findByUserAndDiscountInfo(user, discountInfo);

        if (existingLike.isPresent()) {
            // 이미 객체가 존재한다면
            if (existingLike.get().getIsLike()) {
                // 좋아요를 누른 상태라면
                existingLike.get().toggleLike();
                discountInfo.subLikeCount();
            } else {
                // 좋아요를 누르지 않은 상태라면
                existingLike.get().toggleLike();
                discountInfo.addLikeCount();
            }
        } else {
            // 아직 좋아요를 누르지 않은 상태라면
            DiscountInfoLike newLike = DiscountInfoLike.builder()
                .user(user)
                .discountInfo(discountInfo)
                .isLike(true)
                .build();
            discountInfoLikeRepository.save(newLike);
            discountInfo.addLikeCount();
        }

        DiscountInfo savedEntity = discountInfoRepository.save(discountInfo);

        List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);

        return discountInfoConverter.toDto(savedEntity, connectedInfos);
    }

    @Transactional
    @Override
    public DiscountResponseDto updateDiscountInfo(DiscountRequest.UpdateDiscountDto discountRequestDto) {
        /**
         * 1. 할인정보 조회: 주어진 discountRequestDto의 ID로 DiscountInfo 엔티티를 조회하며, 존재하지 않을 경우 에러를 발생시킵니다.
         * 2. 변경사항 업데이트: 조회된 DiscountInfo 엔티티에 대해 DTO에서 전달된 변경사항을 업데이트합니다.
         * 3. 변경사항 저장: 업데이트된 DiscountInfo 엔티티를 데이터베이스에 저장합니다.
         * 4. 저장된 DiscountInfo와 관련된 ConnectedInfo 리스트 조회: 업데이트된 DiscountInfo와 연결된 ConnectedInfo 리스트를 조회합니다.
         * 5. Entity를 ResponseDto로 변환하여 반환: 업데이트된 DiscountInfo와 관련된 ConnectedInfo 리스트를 포함한 DTO를 반환합니다.
         */
        DiscountInfo discountInfo = discountInfoRepository.findById(discountRequestDto.getId())
            .orElseThrow(() -> new IllegalArgumentException("DiscountInfo not found"));

        discountInfo.update(discountRequestDto); // 변경사항 업데이트

        discountInfoRepository.save(discountInfo); // 변경사항 저장

        List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);

        return discountInfoConverter.toDto(discountInfo, connectedInfos);
    }

    @Transactional
    @Override
    public String deleteDiscountInfo(Long discountInfoId) {
        /**
         * 1. 할인정보 조회: 주어진 discountInfoId로 DiscountInfo 엔티티를 조회하며, 존재하지 않을 경우 에러를 발생시킵니다.
         * 2. 연관된 ConnectedInfo 삭제: 연결된 모든 ConnectedInfo를 삭제하여 연관된 데이터를 제거합니다.
         * 3. DiscountInfo 삭제: DiscountInfo 엔티티를 데이터베이스에서 삭제합니다.
         * 4. 성공 여부 반환: 삭제 작업이 완료된 후 "Success"를 반환합니다.
         */
        DiscountInfo discountInfo = discountInfoRepository.findById(discountInfoId)
            .orElseThrow(() -> new IllegalArgumentException("DiscountInfo not found"));

        // 연결된 ConnectedInfo 삭제 (일단 삭제되지 않도록 주석 처리)
//        connectedInfoRepository.deleteAllByDiscountInfo(discountInfo);

        // DiscountInfo 삭제
        discountInfoRepository.deleteById(discountInfoId);

        return "Success";
    }

    @Transactional
    @Override
    public DiscountResponseDto getDiscountInfoById(Long discountInfoId) {
        /**
         * 1. 할인정보 조회: 주어진 discountInfoId로 DiscountInfo 엔티티를 조회하며, 존재하지 않을 경우 에러를 발생시킵니다.
         * 2. Entity 조회: 조회된 DiscountInfo와 관련된 ConnectedInfo 리스트를 가져옵니다.
         * 3. Entity를 ResponseDto로 변환하여 반환: DiscountInfo와 관련된 ConnectedInfo 리스트를 포함한 DTO를 반환합니다.
         */
        DiscountInfo discountInfo = discountInfoRepository.findById(discountInfoId)
            .orElseThrow(() -> new IllegalArgumentException("DiscountInfo not found"));

        List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);

        return discountInfoConverter.toDto(discountInfo, connectedInfos);
    }

    @Transactional
    @Override
    public Page<DiscountResponseDto> getLikedDiscountInfo(Long userId, Integer page, Integer size) {
        /**
         * 1. 페이징 설정: 주어진 page와 size를 사용하여 Pageable 객체를 생성합니다.
         * 2. 사용자 조회: userId로 User 엔티티를 조회하며, 존재하지 않을 경우 IllegalArgumentException을 발생시킵니다.
         * 3. 좋아요 정보 조회: 조회된 User에 대한 DiscountInfoLike 리스트를 업데이트 시간 기준으로 내림차순 정렬하여 가져옵니다.
         * 4. ResponseDto로 변환: DiscountInfoLike 리스트를 DiscountResponseDto 페이지로 변환하여 반환합니다.
         */

        Pageable pageable = PageRequest.of(page, size);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Page<DiscountInfoLike> likes = discountInfoLikeRepository.findAllByUserOrderByUpdatedAtDesc(user, pageable);

        return discountInfoConverter.toEntityPage(likes);
    }
}
