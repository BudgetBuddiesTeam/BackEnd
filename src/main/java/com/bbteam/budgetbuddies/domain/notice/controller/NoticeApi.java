package com.bbteam.budgetbuddies.domain.notice.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import org.springframework.data.domain.Pageable;

public interface NoticeApi {

    ApiResponse<?> saveNotice(NoticeRequestDto dto);
    ApiResponse<?> findAllWithPaging(Pageable pageable);

    ApiResponse<?> findOne(Long noticeId);
    ApiResponse<?> modifyNotice(Long noticeId, NoticeRequestDto noticeRequestDto);
    ApiResponse<?> deleteNotice(Long noticeId);


}
