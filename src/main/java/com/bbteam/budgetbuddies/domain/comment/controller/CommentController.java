package com.bbteam.budgetbuddies.domain.comment.controller;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // user, discountInfo 인증 어노테이션 추후 추가 예정

    @Operation(summary = "[User] 특정 할인 정보 게시글에 댓글달기", description = "특정 할인 정보 게시글에 댓글을 다는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "현재 댓글을 다는 유저 id입니다."),
            @Parameter(name = "discountInfoId", description = "댓글을 다는 할인 정보 게시글 id입니다."),
            @Parameter(name = "content", description = "댓글 내용입니다."),
    })
    @PostMapping("/discounts/comments/{userId}/add")
    public ResponseEntity<CommentResponseDto.DiscountInfoSuccessDto> saveDiscountInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.DiscountInfoCommentDto discountInfoCommentDto){
        CommentResponseDto.DiscountInfoSuccessDto dto = commentService.saveDiscountComment(userId, discountInfoCommentDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "[User] 특정 할인 정보 게시글의 댓글 조회하기", description = "특정 할인 정보 게시글의 댓글을 가져오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "discountInfoId", description = "댓글을 가져올 할인 정보 게시글 id입니다."),
    })
    @GetMapping("/discounts/comments/get/{discountInfoId}")
    public ResponseEntity<List<CommentResponseDto.DiscountInfoCommentDto>> findAllByDiscountInfo(
            @RequestParam("discountInfoId") Long discountInfoId){
        List<CommentResponseDto.DiscountInfoCommentDto> result = commentService.findByDiscountInfo(discountInfoId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "[User] 특정 지원 정보 게시글에 댓글달기", description = "특정 지원 정보 게시글에 댓글을 다는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "현재 댓글을 다는 유저 id입니다."),
            @Parameter(name = "supportInfoId", description = "댓글을 다는 지원 정보 게시글 id입니다."),
            @Parameter(name = "content", description = "댓글 내용입니다."),
    })
    @PostMapping("/supports/comments/{userId}/add")
    public ResponseEntity<CommentResponseDto.SupportInfoSuccessDto> saveSupportInfoComment(
            @RequestParam("userId") Long userId,
            @RequestBody CommentRequestDto.SupportInfoCommentDto supportInfoCommentDto){
        CommentResponseDto.SupportInfoSuccessDto dto = commentService.saveSupportComment(userId, supportInfoCommentDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "[User] 특정 지원 정보 게시글의 댓글 조회하기", description = "특정 지원 정보 게시글의 댓글을 가져오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "supportInfoId", description = "댓글을 가져올 지원 정보 게시글 id입니다."),
    })
    @GetMapping("/supports/comments/get/{supportInfoId}")
    public ResponseEntity<List<CommentResponseDto.SupportInfoCommentDto>> findAllBySupportInfo(
            @RequestParam("supportInfoId") Long supportInfoId){
        List<CommentResponseDto.SupportInfoCommentDto> result = commentService.findBySupportInfo(supportInfoId);
        return ResponseEntity.ok(result);
    }




}
