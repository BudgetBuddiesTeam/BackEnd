package com.bbteam.budgetbuddies.domain.supportinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportResponseDto {

    private Long id;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer anonymousNumber;

    private Integer likeCount;

    private String siteUrl;

    private String thumbnailUrl;

}
