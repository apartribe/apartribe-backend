package kr.apartribebackend.article.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.global.validator.LevelValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LevelValidator.class)
public @interface IsLevelValid {

    String message() default "유효하지 않은 공지 범위입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
