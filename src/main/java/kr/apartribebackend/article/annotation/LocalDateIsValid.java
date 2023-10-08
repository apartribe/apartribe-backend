package kr.apartribebackend.article.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.article.validator.LocalDateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {LocalDateValidator.class})
public @interface LocalDateIsValid {

    String message() default "날짜는 yyyy-MM-dd 포맷이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String pattern() default "yyyy-MM-dd";

}
