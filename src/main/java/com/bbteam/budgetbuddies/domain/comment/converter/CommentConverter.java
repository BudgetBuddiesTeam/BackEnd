package com.bbteam.budgetbuddies.domain.comment.converter;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.user.entity.User;

import java.util.HashMap;

public class CommentConverter {

    public static Comment toDiscountComment(CommentRequestDto.DiscountInfoCommentDto dto, User user, DiscountInfo discountInfo) {
        return Comment.builder()
                .user(user)
                .discountInfo(discountInfo)
                .content(dto.getContent())
                .build();
    }

    public static Comment toSupportComment(CommentRequestDto.SupportInfoCommentDto dto, User user, SupportInfo supportInfo) {
        return Comment.builder()
                .user(user)
                .supportInfo(supportInfo)
                .content(dto.getContent())
                .build();
    }

    public static CommentResponseDto.DiscountInfoCommentDto toDiscountInfoCommentDto(Comment comment,
                                                                                     HashMap<Long, Long> anonymousMapping){
        return CommentResponseDto.DiscountInfoCommentDto.builder()
                .discountInfoId(comment.getDiscountInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .anonymousNumber(anonymousMapping.get(comment.getUser().getId()))
                .build();

    }

    public static CommentResponseDto.SupportInfoCommentDto toSupportInfoCommentDto(Comment comment,
                                                                                   HashMap<Long, Long> anonymousMapping){
        return CommentResponseDto.SupportInfoCommentDto.builder()
                .supportInfoId(comment.getSupportInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .anonymousNumber(anonymousMapping.get(comment.getUser().getId()))
                .build();

    }

    public static CommentResponseDto.DiscountInfoSuccessDto toDiscountInfoSuccessDto(Comment comment){
        return CommentResponseDto.DiscountInfoSuccessDto.builder()
                .discountInfoId(comment.getDiscountInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }

    public static CommentResponseDto.SupportInfoSuccessDto toSupportInfoSuccessDto(Comment comment){
        return CommentResponseDto.SupportInfoSuccessDto.builder()
                .supportInfoId(comment.getSupportInfo().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .build();
    }

}
