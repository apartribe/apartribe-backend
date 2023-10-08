package kr.apartribebackend.article.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.article.annotation.LocalDateIsValid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// TODO 자주쓰이니 블로그에 옮겨보자.
public class LocalDateValidator implements ConstraintValidator<LocalDateIsValid, String> {

    private String pattern;

    @Override
    public void initialize(LocalDateIsValid constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            final LocalDate parse = LocalDate.parse(value, dateTimeFormatter);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}
