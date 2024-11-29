package com.bbteam.budgetbuddies.domain.supportinfo.converter;

import com.bbteam.budgetbuddies.domain.connectedinfo.entity.ConnectedInfo;
import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfolike.entity.SupportInfoLike;
import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportInfoConverter {

    private final ConnectedInfoRepository connectedInfoRepository;

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

    /**
     * @param Page<SupportInfoLike>
     * @return Page<SupportResponseDto>
     */
    public Page<SupportResponseDto> toEntityPage(Page<SupportInfoLike> likes) {
        return likes.map(like -> {
            // 1. SupportInfoLike 객체에서 SupportInfo를 추출하고, null인지 확인
            SupportInfo supportInfo = like.getSupportInfo();
            if (supportInfo == null) {
                // supportInfo가 null인 경우 null을 반환
                return null;
            }

            // 2. 해당 SupportInfo와 연결된 모든 ConnectedInfo를 connectedInfoRepository를 통해 조회합니다.
            List<ConnectedInfo> connectedInfos = connectedInfoRepository.findAllBySupportInfo(supportInfo);
            if (connectedInfos == null) {
                connectedInfos = Collections.emptyList(); // connectedInfos가 null일 경우 빈 리스트로 처리
            }

            // 3. ConnectedInfo 리스트에서 SupportInfo와 연결된 해시태그를 필터링하고, 해시태그의 이름을 추출합니다.
            List<String> hashtags = connectedInfos.stream()
                // 3-1. SupportInfo와 연결된 ConnectedInfo만 필터링하고, Hashtag가 null이 아닌지 확인
                .filter(connectedInfo -> connectedInfo.getSupportInfo() != null
                    && connectedInfo.getSupportInfo().equals(supportInfo)
                    && connectedInfo.getHashtag() != null)
                .map(ConnectedInfo::getHashtag)
                .map(Hashtag::getName)
                .toList();

            // 4. 추출한 데이터를 기반으로 SupportResponseDto를 생성합니다.
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
        });
    }
}
