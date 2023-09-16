package kr.apartribebackend.global.config.converter;


import kr.apartribebackend.article.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;


@Slf4j
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getName().equals(source))
                .findFirst()
                .orElse(Category.ALL);
    }
}