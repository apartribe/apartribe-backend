package kr.apartribebackend.global.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.global.validator.CategoryValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryValidator.class)
public @interface IsCategoryValid {

    String message() default "유효하지 않은 카테고리 범위입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
