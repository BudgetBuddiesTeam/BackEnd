package com.bbteam.budgetbuddies.domain.notice.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.service.NoticeService;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class NoticeController implements NoticeApi{

    private final NoticeService noticeService;

    @Override
    public ApiResponse<?> saveNotice(@ExistUser Long userId, NoticeRequestDto dto) {
        return ApiResponse.onSuccess(noticeService.save(dto, userId));
    }

    @Override
    public ApiResponse<?> findAllWithPaging(
            @ParameterObject @PageableDefault(page = 0, size = 20,
                    sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(noticeService.findAll(pageable));
    }

    @Override
    public ApiResponse<?> findOne(Long noticeId) {
        return ApiResponse.onSuccess(noticeService.findOne(noticeId));
    }

    @Override
    public ApiResponse<?> modifyNotice(Long noticeId, NoticeRequestDto noticeRequestDto) {
        return ApiResponse.onSuccess(noticeService.update(noticeId, noticeRequestDto));
    }

    @Override
    public ApiResponse<?> deleteNotice(Long noticeId) {
        noticeService.delete(noticeId);
        return ApiResponse.onSuccess("ok");
    }
}
