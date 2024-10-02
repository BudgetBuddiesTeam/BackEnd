package com.bbteam.budgetbuddies.domain.supportinfo.service;

import com.bbteam.budgetbuddies.domain.connectedinfo.repository.ConnectedInfoRepository;
import com.bbteam.budgetbuddies.domain.hashtag.repository.HashtagRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.converter.SupportInfoConverter;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportRequest;
import com.bbteam.budgetbuddies.domain.supportinfo.dto.SupportResponseDto;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfolike.entity.SupportInfoLike;
import com.bbteam.budgetbuddies.domain.supportinfolike.repository.SupportInfoLikeRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import com.bbteam.budgetbuddies.enums.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class SupportInfoServiceTest {

    @Mock
    private SupportInfoRepository supportInfoRepository;

    @Mock
    private SupportInfoLikeRepository supportInfoLikeRepository;

    @Mock
    private SupportInfoConverter supportInfoConverter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectedInfoRepository connectedInfoRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    @InjectMocks
    private SupportInfoServiceImpl supportInfoService;

    @Test
    @DisplayName("지원 정보 2개를 등록한 뒤, 호출했을 때 2개가 정상적으로 반환되는지 검증")
    void getSupportsByYearAndMonthTest() {
        // given
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 31);
        Pageable pageable = PageRequest.of(0, 10);

        SupportInfo support1 = SupportInfo.builder()
            .title("Support 1")
            .startDate(startDate)
            .endDate(endDate)
            .siteUrl("http://example1.com")
            .build();

        SupportInfo support2 = SupportInfo.builder()
            .title("Support 2")
            .startDate(startDate)
            .endDate(endDate)
            .siteUrl("http://example2.com")
            .build();

        List<SupportInfo> supports = List.of(support1, support2);
        Page<SupportInfo> supportPage = new PageImpl<>(supports, pageable, supports.size());

        SupportResponseDto dto1 = new SupportResponseDto();
        SupportResponseDto dto2 = new SupportResponseDto();

        when(connectedInfoRepository.findAllBySupportInfo(support1)).thenReturn(List.of());
        when(connectedInfoRepository.findAllBySupportInfo(support2)).thenReturn(List.of());

        when(supportInfoRepository.findByDateRange(startDate, endDate, pageable)).thenReturn(supportPage);
        when(supportInfoConverter.toDto(support1, List.of())).thenReturn(dto1);
        when(supportInfoConverter.toDto(support2, List.of())).thenReturn(dto2);

        // when
        Page<SupportResponseDto> result = supportInfoService.getSupportsByYearAndMonth(2024, 7, 0, 10);

        // then
        // 1. 데이터가 2개인지 검증
        // 2. 각 데이터 내용이 일치하는지 검증
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0)).isEqualTo(dto1);
        assertThat(result.getContent().get(1)).isEqualTo(dto2);

        // 3. 각 메소드가 1번씩만 호출되었는지 검증
        verify(supportInfoRepository, times(1)).findByDateRange(startDate, endDate, pageable);
        verify(supportInfoConverter, times(1)).toDto(support1, List.of());
        verify(supportInfoConverter, times(1)).toDto(support2, List.of());
    }

    @Test
    @DisplayName("지원 정보 등록이 정상적으로 되는지 검증")
    void registerSupportInfoTest() {
        // given
        SupportRequest.RegisterSupportDto requestDto = SupportRequest.RegisterSupportDto.builder()
            .title("지원 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .siteUrl("http://example.com")
            .build();

        SupportInfo entity = SupportInfo.builder()
            .title("지원 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .siteUrl("http://example.com")
            .build();

        SupportResponseDto responseDto = SupportResponseDto.builder()
            .id(1L)
            .title("지원 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .siteUrl("http://example.com")
            .likeCount(0)
            .anonymousNumber(0)
            .build();

        when(supportInfoConverter.toEntity(requestDto)).thenReturn(entity);
        when(supportInfoRepository.save(entity)).thenReturn(entity);
        when(supportInfoConverter.toDto(entity, List.of())).thenReturn(responseDto);

        // when
        SupportResponseDto result = supportInfoService.registerSupportInfo(requestDto);

        // then
        // 1. 데이터 내용이 일치하는지 검증
        assertThat(result).isEqualTo(responseDto);

        // 2. 각 메소드가 1번씩만 호출되었는지 검증
        verify(supportInfoConverter, times(1)).toEntity(requestDto);
        verify(supportInfoRepository, times(1)).save(entity);
        verify(supportInfoConverter, times(1)).toDto(entity, List.of());
    }

    @Test
    @DisplayName("좋아요를 처음 눌렀을 때 1을 반환하는지 검증")
    void toggleLikeTest() {
        // given
        User user = User.builder()
            .id(1L)
            .phoneNumber("010-1234-5678")
            .name("John")
            .age(30)
            .gender(Gender.MALE)
            .email("john.doe@example.com")
            .photoUrl("http://example.com/photo.jpg")
            .consumptionPattern("Regular")
            .lastLoginAt(LocalDateTime.now())
            .build();

        SupportInfo supportInfo = SupportInfo.builder()
            .id(1L)
            .title("지원 정보 제목")
            .startDate(LocalDate.of(2024, 7, 20))
            .endDate(LocalDate.of(2024, 7, 30))
            .likeCount(0)
            .anonymousNumber(0)
            .siteUrl("http://example.com")
            .build();

        SupportInfoLike supportInfoLike = SupportInfoLike.builder()
            .id(1L)
            .user(user)
            .supportInfo(supportInfo)
            .isLike(false)
            .build();

        SupportResponseDto responseDto = SupportResponseDto.builder()
            .id(1L)
            .title("지원 정보 제목")
            .startDate(LocalDate.of(2024, 7, 20))
            .endDate(LocalDate.of(2024, 7, 30))
            .likeCount(1)
            .anonymousNumber(0)
            .siteUrl("http://example.com")
            .build();

        when(connectedInfoRepository.findAllBySupportInfo(supportInfo)).thenReturn(List.of());
        when(supportInfoRepository.save(any(SupportInfo.class))).thenReturn(supportInfo);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(supportInfoRepository.findById(anyLong())).thenReturn(Optional.of(supportInfo));
        when(supportInfoLikeRepository.findByUserAndSupportInfo(user, supportInfo)).thenReturn(Optional.of(supportInfoLike));
        when(supportInfoConverter.toDto(any(SupportInfo.class), anyList())).thenReturn(responseDto);

        // when
        SupportResponseDto result = supportInfoService.toggleLike(1L, 1L);

        // then
        // 1. 결과 객체 비교 검증
        assertThat(result).isEqualTo(responseDto);

        // 2. 좋아요 개수 0 -> 1로 증가했는지 검증
        assertThat(supportInfo.getLikeCount()).isEqualTo(1);

        // 3. 각 메소드가 1번씩만 호출되었는지 검증
        verify(supportInfoRepository, times(1)).findById(1L);
        verify(supportInfoRepository, times(1)).save(supportInfo);
        verify(supportInfoConverter, times(1)).toDto(supportInfo, List.of());
    }

}