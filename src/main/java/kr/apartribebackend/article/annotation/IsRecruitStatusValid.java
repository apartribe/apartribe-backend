package kr.apartribebackend.article.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.article.validator.RecruitStatusValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = RecruitStatusValidator.class)
public @interface IsRecruitStatusValid {

    String message() default "유효하지 않은 모집 상태입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
