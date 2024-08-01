package com.bbteam.budgetbuddies.domain.supportinfo.entity;

import com.bbteam.budgetbuddies.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class SupportInfo extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Integer likeCount = 0;

    @Builder.Default
    private Integer anonymousNumber = 0;

    @Column(length = 1000)
    private String siteUrl;

    @Column(length = 1000)
    private String thumbnailUrl; // 카드 썸네일 이미지

    public void addLikeCount() {
        this.likeCount++;
    }

    public void subLikeCount() {
        this.likeCount--;
    }

    public Integer addAndGetAnonymousNumber() {
        this.anonymousNumber++;
        return anonymousNumber;
    }

}
