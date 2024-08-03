package com.bbteam.budgetbuddies.domain.comment.controller;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public ResponseEntity<CommentResponseDto.DiscountInfoCommentDto> saveDiscountInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.DiscountInfoCommentDto discountInfoCommentDto){
        CommentResponseDto.DiscountInfoCommentDto dto = discountCommentService.saveComment(userId, discountInfoCommentDto);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/discounts/comments")
    public ResponseEntity<Page<CommentResponseDto.DiscountInfoCommentDto>> findAllByDiscountInfo(
            @RequestParam("discountInfoId") Long discountInfoId,
            @PageableDefault(size = 20, page = 0) Pageable pageable){
        Page<CommentResponseDto.DiscountInfoCommentDto> result = discountCommentService.findByInfoWithPaging(discountInfoId, pageable);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/supports/comments")
    public ResponseEntity<CommentResponseDto.SupportInfoCommentDto> saveSupportInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.SupportInfoCommentDto supportInfoCommentDto){
        CommentResponseDto.SupportInfoCommentDto dto = supportCommentService.saveComment(userId, supportInfoCommentDto);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/supports/comments")
    public ResponseEntity<Page<CommentResponseDto.SupportInfoCommentDto>> findAllBySupportInfo(
            @RequestParam("supportInfoId") Long supportInfoId,
            @PageableDefault(size = 20, page = 0)Pageable pageable){
        Page<CommentResponseDto.SupportInfoCommentDto> result = supportCommentService.findByInfoWithPaging(supportInfoId, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comments/delete")
    public ResponseEntity<String> deleteComment(@RequestParam("commentId") Long commentId) {
        discountCommentService.deleteComment(commentId);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/supports/comments/modify")
    public ResponseEntity<CommentResponseDto.SupportInfoCommentDto> findSupportOne(@RequestParam("commentId")Long commentId) {
        CommentResponseDto.SupportInfoCommentDto result = supportCommentService.findCommentOne(commentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/supports/comments/modify")
    public ResponseEntity<CommentResponseDto.SupportInfoCommentDto> modifySupportOne(
            @RequestBody CommentRequestDto.CommentModifyDto dto) {
        CommentResponseDto.SupportInfoCommentDto result = supportCommentService.modifyComment(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/discounts/comments/modify")
    public ResponseEntity<CommentResponseDto.DiscountInfoCommentDto> findDiscountOne(@RequestParam("commentId")Long commentId) {
        CommentResponseDto.DiscountInfoCommentDto result = discountCommentService.findCommentOne(commentId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/discounts/comments/modify")
    public ResponseEntity<CommentResponseDto.DiscountInfoCommentDto> modifyDiscountOne(
            @RequestBody CommentRequestDto.CommentModifyDto dto) {
        CommentResponseDto.DiscountInfoCommentDto result = discountCommentService.modifyComment(dto);
        return ResponseEntity.ok(result);
    }



}
