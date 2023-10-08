package kr.apartribebackend.article.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kr.apartribebackend.category.repository.CategoryRepository;
import kr.apartribebackend.article.annotation.IsCategoryValid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CategoryValidator implements ConstraintValidator<IsCategoryValid, String> {

    private final CategoryRepository categoryRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        return categoryRepository.findByName(value).isPresent();
        return false;
    }
}
