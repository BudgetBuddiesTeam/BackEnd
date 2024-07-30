package com.bbteam.budgetbuddies.domain.comment.controller;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController implements CommentControllerApi {

    private final CommentService commentService;

    @PostMapping("/discounts/comments")
    public ResponseEntity<CommentResponseDto.DiscountInfoSuccessDto> saveDiscountInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.DiscountInfoCommentDto discountInfoCommentDto){
        CommentResponseDto.DiscountInfoSuccessDto dto = commentService.saveDiscountComment(userId, discountInfoCommentDto);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/discounts/comments")
    public ResponseEntity<Page<CommentResponseDto.DiscountInfoCommentDto>> findAllByDiscountInfo(
            @RequestParam("discountInfoId") Long discountInfoId,
            @PageableDefault(size = 20, page = 0) Pageable pageable){
        Page<CommentResponseDto.DiscountInfoCommentDto> result = commentService.findByDiscountInfoWithPaging(discountInfoId, pageable);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/supports/comments")
    public ResponseEntity<CommentResponseDto.SupportInfoSuccessDto> saveSupportInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.SupportInfoCommentDto supportInfoCommentDto){
        CommentResponseDto.SupportInfoSuccessDto dto = commentService.saveSupportComment(userId, supportInfoCommentDto);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/supports/comments")
    public ResponseEntity<Page<CommentResponseDto.SupportInfoCommentDto>> findAllBySupportInfo(
            @RequestParam("supportInfoId") Long supportInfoId,
            @PageableDefault(size = 20, page = 0)Pageable pageable){
        Page<CommentResponseDto.SupportInfoCommentDto> result = commentService.findBySupportInfoWithPaging(supportInfoId, pageable);
        return ResponseEntity.ok(result);
    }


    public ResponseEntity<String> deleteComment(@RequestParam("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("ok");
    }

}
