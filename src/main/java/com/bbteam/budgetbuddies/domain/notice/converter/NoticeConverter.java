package com.bbteam.budgetbuddies.domain.notice.converter;

import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeResponseDto;
import com.bbteam.budgetbuddies.domain.notice.entity.Notice;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public class NoticeConverter {

    public static Notice toEntity(NoticeRequestDto dto, User user) {
        return Notice.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .user(user)
                .build();
    }

    public static NoticeResponseDto toDto(Notice notice) {
        return NoticeResponseDto.builder()
                .noticeId(notice.getId())
                .title(notice.getTitle())
                .body(notice.getBody())
                .userName(notice.getUser().getName())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
