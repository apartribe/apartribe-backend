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
        return categoryRepository.findByName(value).isPresent();
    }
}


//package kr.apartribebackend.global.validator;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import kr.apartribebackend.article.domain.Category;
//import kr.apartribebackend.article.annotation.IsCategoryValid;
//
//import java.util.Arrays;
//
//public class CategoryValidator implements ConstraintValidator<IsCategoryValid, String> {
//
//    @Override
//    public boolean isValid(String value, ConstraintValidatorContext context) {
//        return Arrays.stream(Category.values())
//                .anyMatch(category -> category.getName().equals(value));
//    }
//}
