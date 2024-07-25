package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    CommentResponseDto.SupportInfoSuccessDto saveSupportComment(Long userId, CommentRequestDto.SupportInfoCommentDto dto);
    CommentResponseDto.DiscountInfoSuccessDto saveDiscountComment(Long userId, CommentRequestDto.DiscountInfoCommentDto dto);


    /**
     *
     * @param discountInfoId
     * @return List<CommentResponseDto.DiscountInfoCommentDto>
     * 해당 로직은 익명 구분을 위한 익명 구분 숫자도 같이 return 합니다.
     */
    List<CommentResponseDto.DiscountInfoCommentDto> findByDiscountInfo(Long discountInfoId);

    /**
     *
     * @param supportInfoId
     * @return List<CommentResponseDto.SupportInfoCommentDto>
     * 해당 로직은 익명 구분을 위한 익명 구분 숫자도 같이 return 합니다.
     */
    List<CommentResponseDto.SupportInfoCommentDto> findBySupportInfo(Long supportInfoId);

    Page<CommentResponseDto.DiscountInfoCommentDto> findByDiscountInfoWithPaging(Long discountInfoId, Pageable pageable);
    Page<CommentResponseDto.SupportInfoCommentDto> findBySupportInfoWithPaging(Long supportInfoId, Pageable pageable);

    void deleteComment(Long commentId);





}
