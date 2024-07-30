package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.converter.CommentConverter;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AbstractCommentService<R, T> implements CommentService<R, T> {

    protected final CommentRepository commentRepository;

    public AbstractCommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No such id"));
        commentRepository.delete(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }
}
