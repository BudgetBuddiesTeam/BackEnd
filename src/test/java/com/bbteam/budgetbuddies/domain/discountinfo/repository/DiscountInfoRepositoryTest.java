package com.bbteam.budgetbuddies.domain.discountinfo.repository;

import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiscountInfoRepositoryTest {

    @Autowired
    private DiscountInfoRepository discountInfoRepository;

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
}