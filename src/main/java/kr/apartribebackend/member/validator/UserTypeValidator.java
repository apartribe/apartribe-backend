package kr.apartribebackend.member.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.member.annotation.IsUserTypeValid;
import kr.apartribebackend.member.domain.UserType;

import java.util.Arrays;

public class UserTypeValidator implements ConstraintValidator<IsUserTypeValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(UserType.values())
                .anyMatch(userType -> userType.getName().equals(value));
    }
}
