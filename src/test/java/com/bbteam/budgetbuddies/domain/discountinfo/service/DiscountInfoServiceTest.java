package com.bbteam.budgetbuddies.domain.discountinfo.service;

import com.bbteam.budgetbuddies.domain.discountinfo.converter.DiscountInfoConverter;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountRequest;
import com.bbteam.budgetbuddies.domain.discountinfo.dto.DiscountResponseDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.discountinfolike.entity.DiscountInfoLike;
import com.bbteam.budgetbuddies.domain.discountinfolike.repository.DiscountInfoLikeRepository;
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
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class DiscountInfoServiceTest {

    @Mock
    private DiscountInfoRepository discountInfoRepository;

    @Mock
    private DiscountInfoLikeRepository discountInfoLikeRepository;

    @Mock
    private DiscountInfoConverter discountInfoConverter;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DiscountInfoServiceImpl discountInfoService;

    @Test
    @DisplayName("할인 정보 2개를 등록한 뒤, 호출했을 때 2개가 정상적으로 반환되는지 검증")
    void getDiscountsByYearAndMonthTest() {
        // given
        LocalDate startDate = LocalDate.of(2024, 7, 1);
        LocalDate endDate = LocalDate.of(2024, 7, 31);
        Pageable pageable = PageRequest.of(0, 10);

        DiscountInfo discount1 = DiscountInfo.builder()
            .title("Discount 1")
            .startDate(startDate)
            .endDate(endDate)
            .discountRate(20)
            .siteUrl("http://example1.com")
            .build();

        DiscountInfo discount2 = DiscountInfo.builder()
            .title("Discount 2")
            .startDate(startDate)
            .endDate(endDate)
            .discountRate(30)
            .siteUrl("http://example2.com")
            .build();

        List<DiscountInfo> discounts = List.of(discount1, discount2);
        Page<DiscountInfo> discountPage = new PageImpl<>(discounts, pageable, discounts.size());

        DiscountResponseDto dto1 = new DiscountResponseDto();
        DiscountResponseDto dto2 = new DiscountResponseDto();

        when(discountInfoRepository.findByDateRange(startDate, endDate, pageable)).thenReturn(discountPage);
        when(discountInfoConverter.toDto(discount1)).thenReturn(dto1);
        when(discountInfoConverter.toDto(discount2)).thenReturn(dto2);

        // when
        Page<DiscountResponseDto> result = discountInfoService.getDiscountsByYearAndMonth(2024, 7, 0, 10);

        // then
        // 1. 데이터가 2개인지 검증
        // 2. 각 데이터 내용이 일치하는지 검증
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0)).isEqualTo(dto1);
        assertThat(result.getContent().get(1)).isEqualTo(dto2);

        // 3. 각 메소드가 1번씩만 호출되었는지 검증
        verify(discountInfoRepository, times(1)).findByDateRange(startDate, endDate, pageable);
        verify(discountInfoConverter, times(1)).toDto(discount1);
        verify(discountInfoConverter, times(1)).toDto(discount2);
    }

    @Test
    @DisplayName("할인 정보 등록이 정상적으로 되는지 검증")
    void registerDiscountInfoTest() {
        // given
        DiscountRequest.RegisterDiscountDto requestDto = DiscountRequest.RegisterDiscountDto.builder()
            .title("할인 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(30)
            .siteUrl("http://example.com")
            .thumbnailUrl("http://example.com2")
            .build();

        DiscountInfo entity = DiscountInfo.builder()
            .title("할인 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(30)
            .siteUrl("http://example.com")
            .thumbnailUrl("http://example.com2")
            .build();

        DiscountResponseDto responseDto = DiscountResponseDto.builder()
            .id(1L)
            .title("할인 정보 제목")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(30)
            .siteUrl("http://example.com")
            .thumbnailUrl("http://example.com2")
            .likeCount(0)
            .anonymousNumber(0)
            .build();

        when(discountInfoConverter.toEntity(requestDto)).thenReturn(entity);
        when(discountInfoRepository.save(entity)).thenReturn(entity);
        when(discountInfoConverter.toDto(entity)).thenReturn(responseDto);

        // when
        DiscountResponseDto result = discountInfoService.registerDiscountInfo(requestDto);

        // then
        // 1. 데이터 내용이 일치하는지 검증
        assertThat(result).isEqualTo(responseDto);

        // 2. 각 메소드가 1번씩만 호출되었는지 검증
        verify(discountInfoConverter, times(1)).toEntity(requestDto);
        verify(discountInfoRepository, times(1)).save(entity);
        verify(discountInfoConverter, times(1)).toDto(entity);
    }

    @Test
    @DisplayName("좋아요를 처음 눌렀을 때 1을 반환하는지 검증")
    void toggleLikeTest() {
        // given
        User user = User.builder()
            .id(1L)
            .phoneNumber("010-1234-5678")
            .name("John Doe")
            .age(30)
            .gender(Gender.MALE)
            .email("john.doe@example.com")
            .photoUrl("http://example.com/photo.jpg")
            .consumptionPattern("Regular")
            .lastLoginAt(LocalDateTime.now())
            .build();

        DiscountInfo discountInfo = DiscountInfo.builder()
            .id(1L)
            .title("Discount on electronics")
            .startDate(LocalDate.of(2024, 7, 20))
            .endDate(LocalDate.of(2024, 7, 30))
            .likeCount(0)
            .anonymousNumber(0)
            .discountRate(50)
            .siteUrl("http://example.com")
            .build();

        DiscountInfoLike discountInfoLike = DiscountInfoLike.builder()
            .id(1L)
            .user(user)
            .discountInfo(discountInfo)
            .isLike(false)
            .build();

        DiscountResponseDto responseDto = DiscountResponseDto.builder()
            .id(1L)
            .title("Discount on electronics")
            .startDate(LocalDate.of(2024, 7, 20))
            .endDate(LocalDate.of(2024, 7, 30))
            .likeCount(1)
            .anonymousNumber(0)
            .discountRate(50)
            .siteUrl("http://example.com")
            .build();

        when(discountInfoRepository.save(any(DiscountInfo.class))).thenReturn(discountInfo);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(discountInfoRepository.findById(anyLong())).thenReturn(Optional.of(discountInfo));
        when(discountInfoLikeRepository.findByUserAndDiscountInfo(user, discountInfo)).thenReturn(Optional.of(discountInfoLike));
        when(discountInfoConverter.toDto(any(DiscountInfo.class))).thenReturn(responseDto);

        // when
        DiscountResponseDto result = discountInfoService.toggleLike(1L, 1L);

        // then
        // 1. 결과 객체 비교 검증
        assertThat(result).isEqualTo(responseDto);

        // 2. 좋아요 개수 0 -> 1로 증가했는지 검증
        assertThat(discountInfo.getLikeCount()).isEqualTo(1);

        // 3. 각 메소드가 1번씩만 호출되었는지 검증
        verify(discountInfoRepository, times(1)).findById(1L);
        verify(discountInfoRepository, times(1)).save(discountInfo);
        verify(discountInfoConverter, times(1)).toDto(discountInfo);
    }



}