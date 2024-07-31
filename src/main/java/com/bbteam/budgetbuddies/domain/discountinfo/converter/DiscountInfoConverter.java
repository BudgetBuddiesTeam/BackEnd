package com.bbteam.budgetbuddies.domain.discountinfo.converter;

import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import org.springframework.stereotype.Service;

@Service
public class DiscountInfoConverter {

    /**
     * @param entity
     * @return responseDto
     */
    public DiscountResponseDto toDto(DiscountInfo entity) {

        return DiscountResponseDto.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .anonymousNumber(entity.getAnonymousNumber())
            .discountRate(entity.getDiscountRate())
            .likeCount(entity.getLikeCount())
            .siteUrl(entity.getSiteUrl())
            .thumbnailUrl(entity.getThumbnailUrl())
            .build();
    }

    /**
     *
     * @param requestDto
     * @return entity
     */
    public DiscountInfo toEntity(DiscountRequest.RegisterDto requestDto) {

        return DiscountInfo.builder()
            .title(requestDto.getTitle())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .anonymousNumber(0)
            .discountRate(requestDto.getDiscountRate())
            .likeCount(0)
            .siteUrl(requestDto.getSiteUrl())
            .thumbnailUrl(requestDto.getThumbnailUrl())
            .build();
    }

    /**
     *
     * @param requestDto
     * @return entity
     */
    public DiscountInfo toEntity(DiscountRequest.UpdateDto requestDto) {

        return DiscountInfo.builder()
            .title(requestDto.getTitle())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .anonymousNumber(0)
            .discountRate(requestDto.getDiscountRate())
            .likeCount(0)
            .siteUrl(requestDto.getSiteUrl())
            .thumbnailUrl(requestDto.getThumbnailUrl())
            .build();
    }

}
