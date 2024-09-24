package com.bbteam.budgetbuddies.domain.discountinfo.converter;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountInfoConverter {

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

}
