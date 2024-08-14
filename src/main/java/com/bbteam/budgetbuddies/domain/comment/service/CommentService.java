package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService<R, T> {


    T saveComment(Long userId, R dto);


    List<T> findByInfo(Long infoId);


    Page<T> findByInfoWithPaging(Long infoId, Pageable pageable);


    void deleteComment(Long commentId);

    T findCommentOne(Long commentId);



    T modifyComment(CommentRequestDto.CommentModifyRequestDto dto);

    Optional<Comment> findById(Long commentId);
}
