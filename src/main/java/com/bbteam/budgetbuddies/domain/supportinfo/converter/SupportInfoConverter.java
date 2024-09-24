package com.bbteam.budgetbuddies.domain.supportinfo.converter;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportInfoConverter {
    /**
     * @param entity
     * @return responseDto
     */
    public SupportResponseDto toDto(SupportInfo supportInfo, List<ConnectedInfo> connectedInfos) {
        // 특정 지원정보의 해시태그 가져오기
        List<String> hashtags = connectedInfos.stream()
            .filter(connectedInfo -> connectedInfo.getSupportInfo() != null && connectedInfo.getSupportInfo().equals(supportInfo)) // 특정 SupportInfo와 연결된 ConnectedInfo만 필터링
            .map(ConnectedInfo::getHashtag)
            .map(Hashtag::getName)
            .collect(Collectors.toList());

        return SupportResponseDto.builder()
            .id(supportInfo.getId())
            .title(supportInfo.getTitle())
            .startDate(supportInfo.getStartDate())
            .endDate(supportInfo.getEndDate())
            .anonymousNumber(supportInfo.getAnonymousNumber())
            .likeCount(supportInfo.getLikeCount())
            .siteUrl(supportInfo.getSiteUrl())
            .thumbnailUrl(supportInfo.getThumbnailUrl())
            .hashtags(hashtags)
            .build();
    }

    /**
     *
     * @param requestDto
     * @return entity
     */
    public SupportInfo toEntity(SupportRequest.RegisterSupportDto requestDto) {

        return SupportInfo.builder()
            .title(requestDto.getTitle())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .anonymousNumber(0)
            .likeCount(0)
            .siteUrl(requestDto.getSiteUrl())
            .thumbnailUrl(requestDto.getThumbnailUrl())
            .isInCalendar(requestDto.getIsInCalendar())
            .build();
    }
}
