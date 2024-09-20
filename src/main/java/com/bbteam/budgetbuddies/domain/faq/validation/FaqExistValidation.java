package com.bbteam.budgetbuddies.domain.faq.validation;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import com.bbteam.budgetbuddies.domain.faq.entity.Faq;
import com.bbteam.budgetbuddies.domain.faq.repository.FaqRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class FaqExistValidation implements ConstraintValidator<ExistFaq, Long> {

    private final FaqRepository faqRepository;
    @Override
    public boolean isValid(Long faqId, ConstraintValidatorContext constraintValidatorContext) {

        Optional<Faq> faq = faqRepository.findById(faqId);

        if(faq.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(ErrorStatus._FAQ_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }

        return true;

    }

    @Override
    public void initialize(ExistFaq constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
