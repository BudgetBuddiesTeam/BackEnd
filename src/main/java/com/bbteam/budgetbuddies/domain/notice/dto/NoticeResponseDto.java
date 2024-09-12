package com.bbteam.budgetbuddies.domain.notice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class NoticeResponseDto {
    private Long noticeId;
    private String userName;
    private String title;
    private String body;
    private LocalDateTime updatedAt;
}
