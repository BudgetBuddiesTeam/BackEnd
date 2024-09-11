package com.bbteam.budgetbuddies.domain.notice.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NoticeController implements NoticeApi{

    private final NoticeService noticeService;

    @Override
    public ApiResponse<?> saveNotice(NoticeRequestDto dto) {
        return null;
    }

    @Override
    public ApiResponse<?> findAllWithPaging(Pageable pageable) {
        return null;
    }

    @Override
    public ApiResponse<?> findOne(Long noticeId) {
        return null;
    }

    @Override
    public ApiResponse<?> modifyNotice(Long noticeId, NoticeRequestDto noticeRequestDto) {
        return null;
    }

    @Override
    public ApiResponse<?> deleteNotice(Long noticeId) {
        return null;
    }
}
