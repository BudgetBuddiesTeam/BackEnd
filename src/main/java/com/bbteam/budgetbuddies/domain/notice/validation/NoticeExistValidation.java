package com.bbteam.budgetbuddies.domain.notice.validation;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.domain.notice.entity.Notice;
import com.bbteam.budgetbuddies.domain.notice.repository.NoticeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class NoticeExistValidation implements ConstraintValidator<ExistNotice, Long> {

    private final NoticeRepository noticeRepository;
    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Notice> notice = noticeRepository.findById(aLong);

        if(notice.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ErrorStatus.NOTICE_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public void initialize(ExistNotice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
