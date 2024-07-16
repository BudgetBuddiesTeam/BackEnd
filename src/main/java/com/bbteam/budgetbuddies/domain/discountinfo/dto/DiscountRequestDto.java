package com.bbteam.budgetbuddies.domain.discountinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequestDto {

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer discountRate;

    private String siteUrl;

}
