package com.bbteam.budgetbuddies.domain.supportinfo.repository;

import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SupportInfoRepositoryTest {

    @Autowired
    SupportInfoRepository supportInfoRepository;


    @Test
    void findByMonthTest(){
        LocalDate start1 = LocalDate.of(2024, 7, 27);
        LocalDate start2 = LocalDate.of(2024, 7, 20);
        LocalDate start3 = LocalDate.of(2024, 8, 31);
        LocalDate start4 = LocalDate.of(2024, 7, 20);
        LocalDate start5 = LocalDate.of(2024, 8, 30);

        LocalDate end1 = LocalDate.of(2024, 8, 5);
        LocalDate end2 = LocalDate.of(2024, 8, 3);
        LocalDate end3 = LocalDate.of(2024, 9, 3);
        LocalDate end4 = LocalDate.of(2024, 7, 24);
        LocalDate end5 = LocalDate.of(2024, 9, 5);

        SupportInfo info1 = SupportInfo.builder()
                .startDate(start1)
                .endDate(end1)
                .title("test1")
                .likeCount(0)
                .build();

        SupportInfo info2 = SupportInfo.builder()
                .startDate(start2)
                .endDate(end2)
                .title("test2")
                .likeCount(0)
                .build();

        SupportInfo info3 = SupportInfo.builder()
                .startDate(start3)
                .endDate(end3)
                .title("test3")
                .likeCount(0)
                .build();

        SupportInfo info4 = SupportInfo.builder()
                .startDate(start4)
                .endDate(end4)
                .title("test4")
                .likeCount(0)
                .build();

        SupportInfo info5 = SupportInfo.builder()
                .startDate(start5)
                .endDate(end5)
                .title("test5")
                .likeCount(0)
                .build();

        supportInfoRepository.save(info1);
        supportInfoRepository.save(info2);
        supportInfoRepository.save(info3);
        supportInfoRepository.save(info4);
        supportInfoRepository.save(info5);

        LocalDate firstDay = LocalDate.of(2024, 8, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<SupportInfo> result = supportInfoRepository.findByMonth(firstDay, lastDay);

        Assertions.assertThat(result.size()).isEqualTo(4);
        Assertions.assertThat(result).containsExactlyInAnyOrder(info1, info2, info3, info5);
    }

    @Test
    void findRecommendInfoByMonthTest(){
        LocalDate start1 = LocalDate.of(2024, 8, 1);
        LocalDate start2 = LocalDate.of(2024, 7, 20);
        LocalDate start3 = LocalDate.of(2024, 8, 31);
        LocalDate start4 = LocalDate.of(2024, 7, 20);
        LocalDate start5 = LocalDate.of(2024, 9, 1);

        LocalDate end1 = LocalDate.of(2024, 8, 5);
        LocalDate end2 = LocalDate.of(2024, 8, 3);
        LocalDate end3 = LocalDate.of(2024, 9, 3);
        LocalDate end4 = LocalDate.of(2024, 7, 24);
        LocalDate end5 = LocalDate.of(2024, 9, 5);

        SupportInfo info1 = SupportInfo.builder()
                .startDate(start1)
                .endDate(end1)
                .title("test1")
                .likeCount(0)
                .build();

        SupportInfo info2 = SupportInfo.builder()
                .startDate(start2)
                .endDate(end2)
                .title("test2")
                .likeCount(0)
                .build();

        SupportInfo info3 = SupportInfo.builder()
                .startDate(start3)
                .endDate(end3)
                .title("test3")
                .likeCount(0)
                .build();

        SupportInfo info4 = SupportInfo.builder()
                .startDate(start4)
                .endDate(end4)
                .title("test4")
                .likeCount(0)
                .build();

        SupportInfo info5 = SupportInfo.builder()
                .startDate(start5)
                .endDate(end5)
                .title("test5")
                .likeCount(0)
                .build();

        supportInfoRepository.save(info1);
        supportInfoRepository.save(info2);
        supportInfoRepository.save(info3);
        supportInfoRepository.save(info4);
        supportInfoRepository.save(info5);

        for(int i = 0; i < 10; i++){
            info1.addLikeCount();
        }
        for(int i = 0; i < 9; i++){
            info2.addLikeCount();
        }
        for(int i = 0; i < 8; i++){
            info3.addLikeCount();
        }
        for(int i = 0; i < 11; i++){
            info4.addLikeCount();
        }
        for(int i = 0; i < 12; i++){
            info5.addLikeCount();
        }
        LocalDate firstDay = LocalDate.of(2024, 8, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<SupportInfo> result = supportInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).containsExactly(info1, info2);

        supportInfoRepository.delete(info3);

        List<SupportInfo> result2 = supportInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result2.size()).isEqualTo(2);
        Assertions.assertThat(result2).containsExactly(info1, info2);

        supportInfoRepository.delete(info1);

        List<SupportInfo> result3 = supportInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result3.size()).isEqualTo(1);
        Assertions.assertThat(result3).containsExactly(info2);


    }
}