package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.converter.CommentConverter;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CommentService<R, T> {


    T saveComment(Long userId, R dto);


    List<T> findByInfo(Long infoId);


    Page<T> findByInfoWithPaging(Long infoId, Pageable pageable);


    void deleteComment(Long commentId);

    T findCommentOne(Long commentId);



    T modifyComment(CommentRequestDto.CommentModifyDto dto);

    Optional<Comment> findById(Long commentId);
}
