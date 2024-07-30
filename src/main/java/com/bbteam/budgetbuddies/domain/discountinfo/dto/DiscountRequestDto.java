package com.bbteam.budgetbuddies.domain.discountinfo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestDto {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer discountRate;

    private String siteUrl;

    private String thumbnailUrl;

}
