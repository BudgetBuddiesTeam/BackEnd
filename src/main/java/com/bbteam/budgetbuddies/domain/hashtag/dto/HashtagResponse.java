package com.bbteam.budgetbuddies.domain.hashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashtagResponse {

    private Long id;

    private String name;

}
