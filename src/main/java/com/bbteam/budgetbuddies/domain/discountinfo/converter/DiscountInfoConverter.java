package com.bbteam.budgetbuddies.domain.discountinfo.converter;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountInfoConverter {

    private final ConnectedInfoRepository connectedInfoRepository;


    /**
     * @param entity
     * @return responseDto
     */
    public DiscountResponseDto toDto(DiscountInfo discountInfo, List<ConnectedInfo> connectedInfos) {
        // 특정 할인정보의 해시태그 가져오기
        List<String> hashtags = connectedInfos.stream()
            .filter(connectedInfo -> connectedInfo.getDiscountInfo() != null && connectedInfo.getDiscountInfo().equals(discountInfo)) // 특정 DiscountInfo와 연결된 ConnectedInfo만 필터링
            .map(ConnectedInfo::getHashtag)
            .map(Hashtag::getName)
            .collect(Collectors.toList());

        return DiscountResponseDto.builder()
            .id(discountInfo.getId())
            .title(discountInfo.getTitle())
            .startDate(discountInfo.getStartDate())
            .endDate(discountInfo.getEndDate())
            .anonymousNumber(discountInfo.getAnonymousNumber())
            .discountRate(discountInfo.getDiscountRate())
            .likeCount(discountInfo.getLikeCount())
            .siteUrl(discountInfo.getSiteUrl())
            .thumbnailUrl(discountInfo.getThumbnailUrl())
            .hashtags(hashtags)
            .build();

    }

    /**
     *
     * @param requestDto
     * @return entity
     */
    public DiscountInfo toEntity(DiscountRequest.RegisterDiscountDto requestDto) {

        return DiscountInfo.builder()
            .title(requestDto.getTitle())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .anonymousNumber(0)
            .discountRate(requestDto.getDiscountRate())
            .likeCount(0)
            .siteUrl(requestDto.getSiteUrl())
            .thumbnailUrl(requestDto.getThumbnailUrl())
            .isInCalendar(requestDto.getIsInCalendar())
            .build();
    }


    /**
     * @param Page<DiscountInfoLike>
     * @return Page<DiscountResponseDto>
     */
    public Page<DiscountResponseDto> toEntityPage(Page<DiscountInfoLike> likes) {

        return likes.map(like -> {
            // 1. DiscountInfoLike 객체에서 DiscountInfo를 추출합니다.
            DiscountInfo discountInfo = like.getDiscountInfo();

            // 2. 해당 DiscountInfo와 연결된 모든 ConnectedInfo를 connectedInfoRepository를 통해 조회합니다.
            List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllByDiscountInfo(discountInfo);

            // 3. ConnectedInfo 리스트에서 DiscountInfo와 연결된 해시태그를 필터링하고, 해시태그의 이름을 추출합니다.
            List<String> hashtags = connectedInfos.stream()
                // 3-1. DiscountInfo와 연결된 ConnectedInfo만 필터링합니다.
                .filter(connectedInfo -> connectedInfo.getDiscountInfo() != null
                    && connectedInfo.getDiscountInfo().equals(discountInfo))
                // 3-2. ConnectedInfo에서 Hashtag 엔티티를 추출하고, 그 해시태그의 이름을 얻습니다.
                .map(ConnectedInfo::getHashtag)
                .map(Hashtag::getName)
                .toList();

            // 4. 추출한 데이터를 기반으로 DiscountResponseDto를 생성합니다.
            return DiscountResponseDto.builder()
                .id(discountInfo.getId())
                .title(discountInfo.getTitle())
                .startDate(discountInfo.getStartDate())
                .endDate(discountInfo.getEndDate())
                .anonymousNumber(discountInfo.getAnonymousNumber())
                .discountRate(discountInfo.getDiscountRate())
                .likeCount(discountInfo.getLikeCount())
                .siteUrl(discountInfo.getSiteUrl())
                .thumbnailUrl(discountInfo.getThumbnailUrl())
                .hashtags(hashtags)
                .build();
        });
    }

}
