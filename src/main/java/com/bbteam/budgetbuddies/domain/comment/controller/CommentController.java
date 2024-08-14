package com.bbteam.budgetbuddies.domain.comment.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.service.CommentService;
import com.bbteam.budgetbuddies.domain.comment.validation.ExistComment;
import com.bbteam.budgetbuddies.domain.user.validation.ExistUser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class CommentController implements CommentControllerApi {

    @Qualifier("discountCommentService")
    private final CommentService<CommentRequestDto.DiscountInfoCommentRequestDto,
        CommentResponseDto.DiscountInfoCommentResponseDto> discountCommentService;

    @Qualifier("supportCommentService")
    private final CommentService<CommentRequestDto.SupportInfoCommentRequestDto,
        CommentResponseDto.SupportInfoCommentResponseDto> supportCommentService;

    public CommentController(CommentService<CommentRequestDto.DiscountInfoCommentRequestDto,
        CommentResponseDto.DiscountInfoCommentResponseDto> discountCommentService,
                             CommentService<CommentRequestDto.SupportInfoCommentRequestDto,
                                 CommentResponseDto.SupportInfoCommentResponseDto> supportCommentService) {
        this.discountCommentService = discountCommentService;
        this.supportCommentService = supportCommentService;
    }

    @PostMapping("/discounts/comments")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentResponseDto> saveDiscountInfoComment(
            @RequestParam("userId") @ExistUser Long userId,
            @RequestBody CommentRequestDto.DiscountInfoCommentRequestDto discountInfoCommentRequestDto){
        CommentResponseDto.DiscountInfoCommentResponseDto dto = discountCommentService.saveComment(userId, discountInfoCommentRequestDto);
        return ApiResponse.onSuccess(dto);
    }


    @GetMapping("/discounts/{discountInfoId}/comments")
    public ApiResponse<Page<CommentResponseDto.DiscountInfoCommentResponseDto>> findAllByDiscountInfo(
            @PathVariable("discountInfoId") Long discountInfoId,
            @PageableDefault(size = 20, page = 0) Pageable pageable){
        Page<CommentResponseDto.DiscountInfoCommentResponseDto> result = discountCommentService.findByInfoWithPaging(discountInfoId, pageable);
        return ApiResponse.onSuccess(result);
    }


    @PostMapping("/supports/comments")
    public ApiResponse<CommentResponseDto.SupportInfoCommentResponseDto> saveSupportInfoComment(
            @RequestParam("userId") @ExistUser Long userId,
            @RequestBody CommentRequestDto.SupportInfoCommentRequestDto supportInfoCommentRequestDto){
        CommentResponseDto.SupportInfoCommentResponseDto dto = supportCommentService.saveComment(userId, supportInfoCommentRequestDto);
        return ApiResponse.onSuccess(dto);
    }


    @GetMapping("/supports/{supportInfoId}/comments")
    public ApiResponse<Page<CommentResponseDto.SupportInfoCommentResponseDto>> findAllBySupportInfo(
            @PathVariable("supportInfoId") Long supportInfoId,
            @PageableDefault(size = 20, page = 0)Pageable pageable){
        Page<CommentResponseDto.SupportInfoCommentResponseDto> result = supportCommentService.findByInfoWithPaging(supportInfoId, pageable);
        return ApiResponse.onSuccess(result);
    }

    @DeleteMapping("/comments/delete/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable("commentId") @ExistComment Long commentId) {
        discountCommentService.deleteComment(commentId);
        return ApiResponse.onSuccess("ok");
    }

    @GetMapping("/supports/comments/getOne/{commentId}")
    public ApiResponse<CommentResponseDto.SupportInfoCommentResponseDto> findSupportOne(@PathVariable("commentId")Long commentId) {
        CommentResponseDto.SupportInfoCommentResponseDto result = supportCommentService.findCommentOne(commentId);
        return ApiResponse.onSuccess(result);
    }

    @PutMapping("/supports/comments/modify")
    public ApiResponse<CommentResponseDto.SupportInfoCommentResponseDto> modifySupportOne(
            @RequestBody CommentRequestDto.CommentModifyRequestDto dto) {
        CommentResponseDto.SupportInfoCommentResponseDto result = supportCommentService.modifyComment(dto);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/discounts/comments/getOne/{commentId}")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentResponseDto> findDiscountOne(@PathVariable("commentId")Long commentId) {
        CommentResponseDto.DiscountInfoCommentResponseDto result = discountCommentService.findCommentOne(commentId);
        return ApiResponse.onSuccess(result);
    }

    @PutMapping("/discounts/comments/modify")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentResponseDto> modifyDiscountOne(
            @RequestBody CommentRequestDto.CommentModifyRequestDto dto) {
        CommentResponseDto.DiscountInfoCommentResponseDto result = discountCommentService.modifyComment(dto);
        return ApiResponse.onSuccess(result);
    }

}
