package kr.apartribebackend.global.config.converter;


import kr.apartribebackend.article.domain.CategoryCondition;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;


@Slf4j @RequiredArgsConstructor
public class CategoryConverter implements Converter<String, CategoryCondition> {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryCondition convert(String source) {
        return categoryRepository.findByName(source)
                .map(Category::getName)
                .map(CategoryCondition::new)
                .orElseGet(() -> new CategoryCondition("전체"));
    }
}

//        return Category.VALUES.getCategories()
//                .stream()
//                .filter(category -> category.getName().equals(source))
//                .findFirst()
//                .map(category -> new CategoryCondition(category.getName()))
//                .orElseGet(() -> new CategoryCondition("전체"));



//package kr.apartribebackend.global.config.converter;
//
//
//import kr.apartribebackend.article.domain.Category;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.convert.converter.Converter;
//
//import java.util.Arrays;
//
//
//@Slf4j
//public class CategoryConverter implements Converter<String, Category> {
//
//    @Override
//    public Category convert(String source) {
//        return Arrays.stream(Category.values())
//                .filter(category -> category.getName().equals(source))
//                .findFirst()
//                .orElse(Category.ALL);
//    }
//}
