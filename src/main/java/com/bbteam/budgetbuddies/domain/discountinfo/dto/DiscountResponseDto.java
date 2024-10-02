package com.bbteam.budgetbuddies.domain.discountinfo.dto;

import com.bbteam.budgetbuddies.domain.hashtag.entity.Hashtag;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    private String thumbnailUrl;

    private List<String> hashtags;

}
