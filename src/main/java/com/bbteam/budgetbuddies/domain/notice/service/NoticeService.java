package com.bbteam.budgetbuddies.domain.notice.service;

import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    NoticeResponseDto save(NoticeRequestDto dto, Long userId);

    NoticeResponseDto findOne(Long noticeId);

    List<NoticeResponseDto> findAll(Pageable pageable);

    NoticeResponseDto update(Long noticeId, NoticeRequestDto dto);

    void delete(Long noticeId);
}
