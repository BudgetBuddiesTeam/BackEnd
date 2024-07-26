package com.bbteam.budgetbuddies.domain.comment.controller;


import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CommentController {
    @Operation(summary = "[User] 특정 할인 정보 게시글에 댓글달기", description = "특정 할인 정보 게시글에 댓글을 다는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "현재 댓글을 다는 유저 id입니다. parameter"),
            @Parameter(name = "discountInfoId", description = "댓글을 다는 할인 정보 게시글 id입니다. requestBody"),
            @Parameter(name = "content", description = "댓글 내용입니다. requestBody"),
    })
    ResponseEntity<CommentResponseDto.DiscountInfoSuccessDto> saveDiscountInfoComment(
            Long userId,
            CommentRequestDto.DiscountInfoCommentDto discountInfoCommentDto);


    @Operation(summary = "[User] 특정 할인 정보 게시글의 댓글 조회하기", description = "특정 할인 정보 게시글의 댓글을 가져오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "discountInfoId", description = "댓글을 가져올 할인 정보 게시글 id입니다. parameter"),
            @Parameter(name = "page", description = "페이징을 위한 페이지 번호입니다. 0부터 시작합니다. parameter"),
            @Parameter(name = "size", description = "페이징을 위한 페이지 사이즈입니다. default는 20입니다. parameter")
    })
    ResponseEntity<Page<CommentResponseDto.DiscountInfoCommentDto>> findAllByDiscountInfo(
            Long discountInfoId,
            Pageable pageable);

    @Operation(summary = "[User] 특정 지원 정보 게시글에 댓글달기", description = "특정 지원 정보 게시글에 댓글을 다는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "userId", description = "현재 댓글을 다는 유저 id입니다. parameter"),
            @Parameter(name = "supportInfoId", description = "댓글을 다는 지원 정보 게시글 id입니다. requestBody"),
            @Parameter(name = "content", description = "댓글 내용입니다. requestBody"),
    })
    ResponseEntity<CommentResponseDto.SupportInfoSuccessDto> saveSupportInfoComment(
            Long userId,
            CommentRequestDto.SupportInfoCommentDto supportInfoCommentDto);

    @Operation(summary = "[User] 특정 지원 정보 게시글의 댓글 조회하기", description = "특정 지원 정보 게시글의 댓글을 가져오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "supportInfoId", description = "댓글을 가져올 지원 정보 게시글 id입니다. parameter"),
            @Parameter(name = "page", description = "페이징을 위한 페이지 번호입니다. 0부터 시작합니다. parameter"),
            @Parameter(name = "size", description = "페이징을 위한 페이지 사이즈입니다. default는 20입니다. parameter")


    })
    ResponseEntity<Page<CommentResponseDto.SupportInfoCommentDto>> findAllBySupportInfo(
            Long supportInfoId,
            Pageable pageable);

    @Operation(summary = "[User] 특정 댓글 삭제하기", description = "특정 댓글을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "commentId", description = "삭제할 댓글 id 입니다. parameter")
    })
    @GetMapping("/comments/delete")
    ResponseEntity<String> deleteComment(Long commentId);
}