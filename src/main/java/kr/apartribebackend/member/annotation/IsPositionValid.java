package kr.apartribebackend.member.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kr.apartribebackend.member.validator.PositionValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositionValidator.class)
public @interface IsPositionValid {
    
    String message() default "position 은 자가, 임대인, 임차인, 청약권자, 관리인, 경비원, 아파트 대표, 동 대표 중 하나여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
