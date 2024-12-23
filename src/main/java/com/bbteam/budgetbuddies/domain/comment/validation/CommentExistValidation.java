package com.bbteam.budgetbuddies.domain.comment.validation;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CommentExistValidation implements ConstraintValidator<ExistComment, Long> {
    private final CommentRepository commentRepository;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Comment> comment = commentRepository.findById(aLong);

        if(comment.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ErrorStatus._COMMENT_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ExistComment constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
