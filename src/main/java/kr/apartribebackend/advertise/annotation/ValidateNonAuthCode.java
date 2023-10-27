package kr.apartribebackend.advertise.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.advertise.validator.NonAuthCodeValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = NonAuthCodeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateNonAuthCode {

    String message() default "nonAuth가 true이면 code 값이 필요하고, false이면 code 값이 있으면 안됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
