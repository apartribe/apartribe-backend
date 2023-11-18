package kr.apartribebackend.member.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.member.annotation.IsPositionValid;
import kr.apartribebackend.member.domain.Position;

import java.util.Arrays;

public class PositionValidator implements ConstraintValidator<IsPositionValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Position.values())
                .anyMatch(position -> position.getName().equals(value));
    }
}
