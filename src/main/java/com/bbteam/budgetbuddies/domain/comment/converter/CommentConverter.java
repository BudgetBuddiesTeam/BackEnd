package com.bbteam.budgetbuddies.domain.comment.converter;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.user.entity.User;

public class CommentConverter {

    public static Comment toDiscountComment(CommentRequestDto.DiscountInfoCommentDto dto, User user, DiscountInfo discountInfo,
                                            Integer anonymousNumber) {
        return Comment.builder()
                .user(user)
                .discountInfo(discountInfo)
                .content(dto.getContent())
                .anonymousNumber(anonymousNumber)
                .build();
    }

    public static Comment toSupportComment(CommentRequestDto.SupportInfoCommentDto dto, User user, SupportInfo supportInfo,
                                           Integer anonymousNumber) {
        return Comment.builder()
                .user(user)
                .supportInfo(supportInfo)
                .content(dto.getContent())
                .anonymousNumber(anonymousNumber)
                .build();
    }

    public static CommentResponseDto.DiscountInfoCommentDto toDiscountInfoCommentDto(Comment comment){
        return CommentResponseDto.DiscountInfoCommentDto.builder()
                .commentId(comment.getId())
                .discountInfoId(comment.getDiscountInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .anonymousNumber(comment.getAnonymousNumber())
                .createdAt(comment.getCreatedAt())
                .build();

    }

    public static CommentResponseDto.SupportInfoCommentDto toSupportInfoCommentDto(Comment comment){
        return CommentResponseDto.SupportInfoCommentDto.builder()
                .commentId(comment.getId())
                .supportInfoId(comment.getSupportInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .anonymousNumber(comment.getAnonymousNumber())
                .createdAt(comment.getCreatedAt())
                .build();

    }

    public static CommentResponseDto.DiscountInfoSuccessDto toDiscountInfoSuccessDto(Comment comment){
        return CommentResponseDto.DiscountInfoSuccessDto.builder()
                .commentId(comment.getId())
                .discountInfoId(comment.getDiscountInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }

    public static CommentResponseDto.SupportInfoSuccessDto toSupportInfoSuccessDto(Comment comment){
        return CommentResponseDto.SupportInfoSuccessDto.builder()
                .commentId(comment.getId())
                .supportInfoId(comment.getSupportInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }

}
