package com.bbteam.budgetbuddies.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class NoticeRequestDto {
    private String title;
    private String body;
}
