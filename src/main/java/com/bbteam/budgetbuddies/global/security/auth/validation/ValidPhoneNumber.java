package com.bbteam.budgetbuddies.global.security.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

    String message() default "전화번호 형식이 유효하지 않습니다. (예: 01012341234)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
