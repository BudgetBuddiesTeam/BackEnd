package com.bbteam.budgetbuddies.domain.calendar.service;

import com.bbteam.budgetbuddies.domain.calendar.converter.CalendarConverter;
import com.bbteam.budgetbuddies.domain.calendar.dto.CalendarDto;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CalendarServiceTest {

    @Autowired
    CalendarService calendarService;
    @Autowired
    DiscountInfoRepository discountInfoRepository;
    @Autowired
    SupportInfoRepository supportInfoRepository;

    @Test
    void findTest(){
        LocalDate discountStart1 = LocalDate.of(2024, 8, 1);
        LocalDate discountStart2 = LocalDate.of(2024, 7, 20);
        LocalDate discountStart3 = LocalDate.of(2024, 8, 31);
        LocalDate discountStart4 = LocalDate.of(2024, 7, 20);
        LocalDate discountStart5 = LocalDate.of(2024, 9, 1);

        LocalDate discountEnd1 = LocalDate.of(2024, 8, 5);
        LocalDate discountEnd2 = LocalDate.of(2024, 8, 3);
        LocalDate discountEnd3 = LocalDate.of(2024, 9, 3);
        LocalDate discountEnd4 = LocalDate.of(2024, 7, 24);
        LocalDate discountEnd5 = LocalDate.of(2024, 9, 5);

        DiscountInfo discountInfo1 = DiscountInfo.builder()
                .startDate(discountStart1)
                .endDate(discountEnd1)
                .title("test1")
                .likeCount(0)
                .build();

        DiscountInfo discountInfo2 = DiscountInfo.builder()
                .startDate(discountStart2)
                .endDate(discountEnd2)
                .title("test2")
                .likeCount(0)
                .build();

        DiscountInfo discountInfo3 = DiscountInfo.builder()
                .startDate(discountStart3)
                .endDate(discountEnd3)
                .title("test3")
                .likeCount(0)
                .build();

        DiscountInfo discountInfo4 = DiscountInfo.builder()
                .startDate(discountStart4)
                .endDate(discountEnd4)
                .title("test4")
                .likeCount(0)
                .build();

        DiscountInfo discountInfo5 = DiscountInfo.builder()
                .startDate(discountStart5)
                .endDate(discountEnd5)
                .title("test5")
                .likeCount(0)
                .build();

        discountInfoRepository.save(discountInfo1);
        discountInfoRepository.save(discountInfo2);
        discountInfoRepository.save(discountInfo3);
        discountInfoRepository.save(discountInfo4);
        discountInfoRepository.save(discountInfo5);

        for(int i = 0; i < 3; i++){
            discountInfo1.addLikeCount();
        }
        for(int i = 0; i < 4; i++){
            discountInfo2.addLikeCount();
        }
        for(int i = 0; i < 5; i++){
            discountInfo3.addLikeCount();
        }
        for(int i = 0; i < 6; i++){
            discountInfo4.addLikeCount();
        }
        for(int i = 0; i < 7; i++){
            discountInfo5.addLikeCount();
        }

        LocalDate firstDay = LocalDate.of(2024, 8, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        LocalDate supportStart1 = LocalDate.of(2024, 8, 1);
        LocalDate supportStart2 = LocalDate.of(2024, 7, 20);
        LocalDate supportStart3 = LocalDate.of(2024, 8, 31);
        LocalDate supportStart4 = LocalDate.of(2024, 7, 20);
        LocalDate supportStart5 = LocalDate.of(2024, 9, 1);

        LocalDate supportEnd1 = LocalDate.of(2024, 8, 5);
        LocalDate supportEnd2 = LocalDate.of(2024, 7, 23);
        LocalDate supportEnd3 = LocalDate.of(2024, 9, 3);
        LocalDate supportEnd4 = LocalDate.of(2024, 7, 24);
        LocalDate supportEnd5 = LocalDate.of(2024, 9, 5);

        SupportInfo supportInfo1 = SupportInfo.builder()
                .startDate(supportStart1)
                .endDate(supportEnd1)
                .title("test1")
                .likeCount(0)
                .build();

        SupportInfo supportInfo2 = SupportInfo.builder()
                .startDate(supportStart2)
                .endDate(supportEnd2)
                .title("test2")
                .likeCount(0)
                .build();

        SupportInfo supportInfo3 = SupportInfo.builder()
                .startDate(supportStart3)
                .endDate(supportEnd3)
                .title("test3")
                .likeCount(0)
                .build();

        SupportInfo supportInfo4 = SupportInfo.builder()
                .startDate(supportStart4)
                .endDate(supportEnd4)
                .title("test4")
                .likeCount(0)
                .build();

        SupportInfo supportInfo5 = SupportInfo.builder()
                .startDate(supportStart5)
                .endDate(supportEnd5)
                .title("test5")
                .likeCount(0)
                .build();

        supportInfoRepository.save(supportInfo1);
        supportInfoRepository.save(supportInfo2);
        supportInfoRepository.save(supportInfo3);
        supportInfoRepository.save(supportInfo4);
        supportInfoRepository.save(supportInfo5);

        for(int i = 0; i < 10; i++){
            supportInfo1.addLikeCount();
        }
        for(int i = 0; i < 9; i++){
            supportInfo2.addLikeCount();
        }
        for(int i = 0; i < 8; i++){
            supportInfo3.addLikeCount();
        }
        for(int i = 0; i < 11; i++){
            supportInfo4.addLikeCount();
        }
        for(int i = 0; i < 12; i++){
            supportInfo5.addLikeCount();
        }

        CalendarDto.CalendarMonthResponseDto result = calendarService.find(2024, 8);
        List<CalendarDto.CalendarDiscountInfoDto> discountInfoDtoList = result.getCalendarMonthInfoDto().getDiscountInfoDtoList();
        List<CalendarDto.CalendarSupportInfoDto> supportInfoDtoList = result.getCalendarMonthInfoDto().getSupportInfoDtoList();

        assertThat(discountInfoDtoList.size()).isEqualTo(3);
        CalendarDto.CalendarDiscountInfoDto discountDto1 = CalendarConverter.toCalendarDiscountInfoDto(discountInfo1);
        CalendarDto.CalendarDiscountInfoDto discountDto2 = CalendarConverter.toCalendarDiscountInfoDto(discountInfo2);
        CalendarDto.CalendarDiscountInfoDto discountDto3 = CalendarConverter.toCalendarDiscountInfoDto(discountInfo3);
        assertThat(discountInfoDtoList).containsExactlyInAnyOrder(discountDto1, discountDto2, discountDto3);

        assertThat(supportInfoDtoList.size()).isEqualTo(2);
        CalendarDto.CalendarSupportInfoDto supportDto1 = CalendarConverter.toCalendarSupportInfoDto(supportInfo1);
        CalendarDto.CalendarSupportInfoDto supportDto3 = CalendarConverter.toCalendarSupportInfoDto(supportInfo3);
        assertThat(supportInfoDtoList).containsExactlyInAnyOrder(supportDto1, supportDto3);

        List<CalendarDto.CalendarDiscountInfoDto> recDiscountDtoList = result.getRecommendMonthInfoDto().getDiscountInfoDtoList();
        List<CalendarDto.CalendarSupportInfoDto> recSupportDtoList = result.getRecommendMonthInfoDto().getSupportInfoDtoList();

        assertThat(recDiscountDtoList.size()).isEqualTo(2);
        assertThat(recDiscountDtoList).containsExactly(discountDto3, discountDto2);

        assertThat(recSupportDtoList.size()).isEqualTo(2);
        assertThat(recSupportDtoList).containsExactly(supportDto1, supportDto3);


    }

}