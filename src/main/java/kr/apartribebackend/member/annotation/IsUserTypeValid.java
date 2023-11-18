package kr.apartribebackend.member.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.member.validator.UserTypeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserTypeValidator.class)
public @interface IsUserTypeValid {

    String message() default "userType 은 resident 혹은 manager 중 하나여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
