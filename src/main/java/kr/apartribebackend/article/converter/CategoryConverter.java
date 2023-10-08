package kr.apartribebackend.article.converter;


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
        return null;
    }
}



//package kr.apartribebackend.article.converter;
//
//
//import kr.apartribebackend.article.domain.CategoryCondition;
//import kr.apartribebackend.category.domain.Category;
//import kr.apartribebackend.category.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.convert.converter.Converter;
//
//
//@Slf4j @RequiredArgsConstructor
//public class CategoryConverter implements Converter<String, CategoryCondition> {
//
//    private final CategoryRepository categoryRepository;
//
//    @Override
//    public CategoryCondition convert(String source) {
//        return categoryRepository.findByName(source)
//                .map(Category::getName)
//                .map(CategoryCondition::new)
//                .orElseGet(() -> new CategoryCondition("전체"));
//    }
//}
