package kr.apartribebackend.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.global.annotation.IsCategoryValid;

import java.util.Arrays;

public class CategoryValidator implements ConstraintValidator<IsCategoryValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Category.values())
                .anyMatch(category -> category.getName().equals(value));
    }
}
