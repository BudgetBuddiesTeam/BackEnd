package com.bbteam.budgetbuddies.domain.notice.controller;


import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeResponseDto;
import com.bbteam.budgetbuddies.domain.notice.service.NoticeService;
import com.bbteam.budgetbuddies.domain.notice.validation.ExistNotice;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/notices")
public class NoticeController implements NoticeApi{

    private final NoticeService noticeService;

    @Override
    @PostMapping
    public ApiResponse<NoticeResponseDto> saveNotice(@ExistUser @RequestParam Long userId, @RequestBody NoticeRequestDto dto) {
        return ApiResponse.onSuccess(noticeService.save(dto, userId));
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<Page<NoticeResponseDto>> findAllWithPaging(
            @ParameterObject @PageableDefault(page = 0, size = 20,
                    sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.onSuccess(noticeService.findAll(pageable));
    }

    @Override
    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeResponseDto> findOne(@ExistNotice @PathVariable Long noticeId) {
        return ApiResponse.onSuccess(noticeService.findOne(noticeId));
    }

    @Override
    @PutMapping("/{noticeId}")
    public ApiResponse<NoticeResponseDto> modifyNotice(@ExistNotice @PathVariable Long noticeId, @RequestBody NoticeRequestDto noticeRequestDto) {
        return ApiResponse.onSuccess(noticeService.update(noticeId, noticeRequestDto));
    }

    @Override
    @DeleteMapping("/{noticeId}")
    public ApiResponse<String> deleteNotice(@ExistNotice @PathVariable Long noticeId) {
        noticeService.delete(noticeId);
        return ApiResponse.onSuccess("ok");
    }
}
