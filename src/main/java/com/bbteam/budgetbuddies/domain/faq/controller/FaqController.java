package com.bbteam.budgetbuddies.domain.faq.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqRequestDto;
import com.bbteam.budgetbuddies.domain.faq.dto.FaqResponseDto;
import com.bbteam.budgetbuddies.domain.faq.service.FaqService;
import com.bbteam.budgetbuddies.domain.faq.validation.ExistFaq;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/faqs")
@RequiredArgsConstructor
public class FaqController implements FaqApi{

    private final FaqService faqService;

    @Override
    @PostMapping("")
    public ApiResponse<FaqResponseDto.FaqPostResponse> postFaq(@ExistUser @RequestParam Long userId,
                                                               @Valid @RequestBody FaqRequestDto.FaqPostRequest dto) {
        return ApiResponse.onSuccess(faqService.postFaq(dto, userId));
    }

    @Override
    @GetMapping("/{faqId}")
    public ApiResponse<FaqResponseDto.FaqFindResponse> findFaq(@PathVariable @ExistFaq Long faqId) {
        return ApiResponse.onSuccess(faqService.findOneFaq(faqId));
    }

    @Override
    @GetMapping("/all")
    public ApiResponse<Page<FaqResponseDto.FaqFindResponse>> findByPaging(@ParameterObject Pageable pageable,
                                                                          @RequestParam @Nullable String searchCondition) {
        return ApiResponse.onSuccess(faqService.searchFaq(pageable, searchCondition));
    }

    @Override
    @PutMapping("/{faqId}")
    public ApiResponse<FaqResponseDto.FaqModifyResponse> modifyFaq(@PathVariable @ExistFaq Long faqId,
                                                                   @Valid @RequestBody FaqRequestDto.FaqModifyRequest dto) {
        return ApiResponse.onSuccess(faqService.modifyFaq(dto, faqId));
    }

    @Override
    @DeleteMapping("/{faqId}")
    public ApiResponse<String> deleteFaq(@PathVariable @ExistFaq Long faqId) {
        faqService.deleteFaq(faqId);
        return ApiResponse.onSuccess("Delete Success");
    }

    @Override
    @PostMapping("/{faqId}/keyword")
    public ApiResponse<?> addKeyword(@PathVariable @ExistFaq Long faqId, @RequestParam Long searchKeywordId) {
        return ApiResponse.onSuccess(faqService.addKeyword(faqId, searchKeywordId));
    }

    @Override
    @DeleteMapping("/{faqId}/keyword")
    public ApiResponse<?> deleteKeyword(@PathVariable @ExistFaq Long faqId, @RequestParam Long searchKeywordId) {
        return ApiResponse.onSuccess(faqService.removeKeyword(faqId, searchKeywordId));
    }
}
