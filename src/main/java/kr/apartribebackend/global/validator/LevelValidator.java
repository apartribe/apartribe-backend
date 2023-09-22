package kr.apartribebackend.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.global.annotation.IsLevelValid;

import java.util.Arrays;

public class LevelValidator implements ConstraintValidator<IsLevelValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Level.values())
                .anyMatch(level -> level.getName().equals(value));
    }
}
