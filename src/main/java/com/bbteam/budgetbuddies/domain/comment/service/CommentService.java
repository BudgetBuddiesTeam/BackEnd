package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto.SupportInfoSuccessDto saveSupportComment(Long userId, CommentRequestDto.SupportInfoCommentDto dto);
    CommentResponseDto.DiscountInfoSuccessDto saveDiscountComment(Long userId, CommentRequestDto.DiscountInfoCommentDto dto);


    List<CommentResponseDto.DiscountInfoCommentDto> findByDiscountInfo(Long discountInfoId);

    List<CommentResponseDto.SupportInfoCommentDto> findBySupportInfo(Long supportInfoId);




}
