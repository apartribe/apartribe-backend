package kr.apartribebackend.article.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.article.annotation.IsCategoryValid;


public class CategoryValidator implements ConstraintValidator<IsCategoryValid, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }
}
