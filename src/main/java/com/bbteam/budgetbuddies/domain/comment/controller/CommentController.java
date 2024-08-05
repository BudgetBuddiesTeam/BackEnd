package com.bbteam.budgetbuddies.domain.comment.controller;

import com.bbteam.budgetbuddies.apiPayload.ApiResponse;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.service.CommentService;
import com.bbteam.budgetbuddies.domain.comment.validation.ExistComment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class CommentController implements CommentControllerApi {

    @Qualifier("discountCommentService")
    private final CommentService<CommentRequestDto.DiscountInfoCommentDto,
            CommentResponseDto.DiscountInfoCommentDto> discountCommentService;

    @Qualifier("supportCommentService")
    private final CommentService<CommentRequestDto.SupportInfoCommentDto,
            CommentResponseDto.SupportInfoCommentDto> supportCommentService;

    public CommentController(CommentService<CommentRequestDto.DiscountInfoCommentDto,
            CommentResponseDto.DiscountInfoCommentDto> discountCommentService,
                             CommentService<CommentRequestDto.SupportInfoCommentDto,
                                       CommentResponseDto.SupportInfoCommentDto> supportCommentService) {
        this.discountCommentService = discountCommentService;
        this.supportCommentService = supportCommentService;
    }

    @PostMapping("/discounts/comments")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentDto> saveDiscountInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.DiscountInfoCommentDto discountInfoCommentDto){
        CommentResponseDto.DiscountInfoCommentDto dto = discountCommentService.saveComment(userId, discountInfoCommentDto);
        return ApiResponse.onSuccess(dto);
    }


    @GetMapping("/discounts/{discountInfoId}/comments")
    public ApiResponse<Page<CommentResponseDto.DiscountInfoCommentDto>> findAllByDiscountInfo(
            @PathVariable("discountInfoId") Long discountInfoId,
            @PageableDefault(size = 20, page = 0) Pageable pageable){
        Page<CommentResponseDto.DiscountInfoCommentDto> result = discountCommentService.findByInfoWithPaging(discountInfoId, pageable);
        return ApiResponse.onSuccess(result);
    }


    @PostMapping("/supports/comments")
    public ApiResponse<CommentResponseDto.SupportInfoCommentDto> saveSupportInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.SupportInfoCommentDto supportInfoCommentDto){
        CommentResponseDto.SupportInfoCommentDto dto = supportCommentService.saveComment(userId, supportInfoCommentDto);
        return ApiResponse.onSuccess(dto);
    }


    @GetMapping("/supports/{supportInfoId}/comments")
    public ApiResponse<Page<CommentResponseDto.SupportInfoCommentDto>> findAllBySupportInfo(
            @PathVariable("supportInfoId") Long supportInfoId,
            @PageableDefault(size = 20, page = 0)Pageable pageable){
        Page<CommentResponseDto.SupportInfoCommentDto> result = supportCommentService.findByInfoWithPaging(supportInfoId, pageable);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/comments/delete/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable("commentId") @ExistComment Long commentId) {
        discountCommentService.deleteComment(commentId);
        return ApiResponse.onSuccess("ok");
    }

    @GetMapping("/supports/comments/getOne/{commentId}")
    public ApiResponse<CommentResponseDto.SupportInfoCommentDto> findSupportOne(@PathVariable("commentId")Long commentId) {
        CommentResponseDto.SupportInfoCommentDto result = supportCommentService.findCommentOne(commentId);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/supports/comments/modify")
    public ApiResponse<CommentResponseDto.SupportInfoCommentDto> modifySupportOne(
            @RequestBody CommentRequestDto.CommentModifyDto dto) {
        CommentResponseDto.SupportInfoCommentDto result = supportCommentService.modifyComment(dto);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/discounts/comments/getOne/{commentId}")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentDto> findDiscountOne(@PathVariable("commentId")Long commentId) {
        CommentResponseDto.DiscountInfoCommentDto result = discountCommentService.findCommentOne(commentId);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/discounts/comments/modify")
    public ApiResponse<CommentResponseDto.DiscountInfoCommentDto> modifyDiscountOne(
            @RequestBody CommentRequestDto.CommentModifyDto dto) {
        CommentResponseDto.DiscountInfoCommentDto result = discountCommentService.modifyComment(dto);
        return ApiResponse.onSuccess(result);
    }



}
