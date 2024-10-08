package com.bbteam.budgetbuddies.global.security.auth.validation;

import com.bbteam.budgetbuddies.apiPayload.code.status.ErrorStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PhoneNumberValidation implements ConstraintValidator<ValidPhoneNumber, String> {

    // 전화번호 형식은 '01012341234'와 같아야 합니다.
    private static final String PHONE_NUMBER_PATTERN = "^010\\d{8}$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        // Check if phone number matches the specified format
        if (phoneNumber == null || !Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)) {
            // 커스텀 에러 코드와 메시지 사용
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                ErrorStatus._PHONE_NUMBER_NOT_VALID.getCode() + ": " + ErrorStatus._PHONE_NUMBER_NOT_VALID.getMessage()
            ).addConstraintViolation();
            return false;
        }
        return true;
    }

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}