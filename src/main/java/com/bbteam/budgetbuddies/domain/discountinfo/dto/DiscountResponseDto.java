package com.bbteam.budgetbuddies.domain.discountinfo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountResponseDto {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer anonymousNumber;

    private Integer discountRate;

    private Integer likeCount;

    private String siteUrl;

}
