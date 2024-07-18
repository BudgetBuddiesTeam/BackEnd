package com.bbteam.budgetbuddies.domain.discountinfo.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class DiscountInfo extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @ColumnDefault("0")
    private Integer likeCount = 0;

    @ColumnDefault("0")
    private Integer anonymousNumber = 0;

    private Integer discountRate;

    @Column(length = 1000)
    private String siteUrl;

}
