package com.bbteam.budgetbuddies.domain.supportinfo.converter;

import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequestDto;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.stereotype.Service;

@Service
public class SupportInfoConverter {
    /**
     * @param entity
     * @return responseDto
     */
    public SupportResponseDto toDto(SupportInfo entity) {

        return SupportResponseDto.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .anonymousNumber(entity.getAnonymousNumber())
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
    public SupportInfo toEntity(SupportRequestDto requestDto) {

        return SupportInfo.builder()
            .title(requestDto.getTitle())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .anonymousNumber(0)
            .likeCount(0)
            .siteUrl(requestDto.getSiteUrl())
            .thumbnailUrl(requestDto.getThumbnailUrl())
            .build();
    }
}
