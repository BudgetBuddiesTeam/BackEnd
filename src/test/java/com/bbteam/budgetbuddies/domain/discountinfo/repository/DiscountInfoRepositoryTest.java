package com.bbteam.budgetbuddies.domain.discountinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import jakarta.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiscountInfoRepositoryTest {

    @Autowired
    private DiscountInfoRepository discountInfoRepository;

    @Test
    @DisplayName("@SoftDelete 테스트")
    void deletedTest() {
        // given
        DiscountInfo discount1 = DiscountInfo.builder()
            .title("할인정보1")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(40)
            .siteUrl("http://example1.com")
            .build();

        DiscountInfo discount2 = DiscountInfo.builder()
            .title("할인정보2")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(40)
            .siteUrl("http://example1.com")
            .build();

        discountInfoRepository.save(discount1);
        discountInfoRepository.save(discount2);

        // when
        discountInfoRepository.deleteById(1L);

        // then
        assertThat(discountInfoRepository.findAll()).hasSize(1);
        assertThat(discountInfoRepository.findAll().get(0).getTitle()).isEqualTo("할인정보2");
    }

    @Test
    @DisplayName("특정 년월에 속하는 할인 정보 데이터 조회 성공")
    void findByDateRangeSuccess() {
        // given
        DiscountInfo discount1 = DiscountInfo.builder()
            .title("할인정보1")
            .startDate(LocalDate.of(2024, 7, 1))
            .endDate(LocalDate.of(2024, 7, 21))
            .discountRate(40)
            .siteUrl("http://example1.com")
            .build();
        discountInfoRepository.save(discount1);

        // when
        Page<DiscountInfo> results = discountInfoRepository.findByDateRange(
            LocalDate.of(2024, 7, 1),
            LocalDate.of(2024, 7, 31),
            PageRequest.of(0, 10)
        );

        // then
        assertThat(results).hasSize(1);
        assertThat(results.getContent().get(0).getTitle()).isEqualTo("할인정보1");
    }

    @Test
    @DisplayName("특정 년월에 속하는 할인 정보 데이터 조회 실패")
    void findByDateRangeFailure() {
        // given
        DiscountInfo discount1 = DiscountInfo.builder()
            .title("할인정보1")
            .startDate(LocalDate.of(2024, 6, 1))
            .endDate(LocalDate.of(2024, 6, 21))
            .discountRate(40)
            .siteUrl("http://example1.com")
            .build();
        discountInfoRepository.save(discount1);

        // when
        Page<DiscountInfo> results = discountInfoRepository.findByDateRange(
            LocalDate.of(2024, 7, 1),
            LocalDate.of(2024, 7, 31),
            PageRequest.of(0, 10)
        );

        // then
        assertThat(results).hasSize(0);
    }

    @Test
    void findByMonthTest(){
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

        DiscountInfo info1 = DiscountInfo.builder()
                .startDate(start1)
                .endDate(end1)
                .title("test1")
                .likeCount(0)
                .build();

        DiscountInfo info2 = DiscountInfo.builder()
                .startDate(start2)
                .endDate(end2)
                .title("test2")
                .likeCount(0)
                .build();

        DiscountInfo info3 = DiscountInfo.builder()
                .startDate(start3)
                .endDate(end3)
                .title("test3")
                .likeCount(0)
                .build();

        DiscountInfo info4 = DiscountInfo.builder()
                .startDate(start4)
                .endDate(end4)
                .title("test4")
                .likeCount(0)
                .build();

        DiscountInfo info5 = DiscountInfo.builder()
                .startDate(start5)
                .endDate(end5)
                .title("test5")
                .likeCount(0)
                .build();

        discountInfoRepository.save(info1);
        discountInfoRepository.save(info2);
        discountInfoRepository.save(info3);
        discountInfoRepository.save(info4);
        discountInfoRepository.save(info5);

        LocalDate firstDay = LocalDate.of(2024, 8, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<DiscountInfo> result = discountInfoRepository.findByMonth(firstDay, lastDay);

        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result).containsExactlyInAnyOrder(info1, info2, info3);
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

        DiscountInfo info1 = DiscountInfo.builder()
                .startDate(start1)
                .endDate(end1)
                .title("test1")
                .likeCount(0)
                .build();

        DiscountInfo info2 = DiscountInfo.builder()
                .startDate(start2)
                .endDate(end2)
                .title("test2")
                .likeCount(0)
                .build();

        DiscountInfo info3 = DiscountInfo.builder()
                .startDate(start3)
                .endDate(end3)
                .title("test3")
                .likeCount(0)
                .build();

        DiscountInfo info4 = DiscountInfo.builder()
                .startDate(start4)
                .endDate(end4)
                .title("test4")
                .likeCount(0)
                .build();

        DiscountInfo info5 = DiscountInfo.builder()
                .startDate(start5)
                .endDate(end5)
                .title("test5")
                .likeCount(0)
                .build();

        discountInfoRepository.save(info1);
        discountInfoRepository.save(info2);
        discountInfoRepository.save(info3);
        discountInfoRepository.save(info4);
        discountInfoRepository.save(info5);

        for(int i = 0; i < 3; i++){
            info1.addLikeCount();
        }
        for(int i = 0; i < 4; i++){
            info2.addLikeCount();
        }
        for(int i = 0; i < 5; i++){
            info3.addLikeCount();
        }
        for(int i = 0; i < 6; i++){
            info4.addLikeCount();
        }
        for(int i = 0; i < 7; i++){
            info5.addLikeCount();
        }
        LocalDate firstDay = LocalDate.of(2024, 8, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        List<DiscountInfo> result = discountInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).containsExactly(info3, info2);

        discountInfoRepository.delete(info3);

        List<DiscountInfo> result2 = discountInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result2.size()).isEqualTo(2);
        Assertions.assertThat(result2).containsExactly(info2, info1);

        discountInfoRepository.delete(info2);

        List<DiscountInfo> result3 = discountInfoRepository.findRecommendInfoByMonth(firstDay, lastDay);

        Assertions.assertThat(result3.size()).isEqualTo(1);
        Assertions.assertThat(result3).containsExactly(info1);


    }

}