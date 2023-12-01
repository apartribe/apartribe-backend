package kr.apartribebackend.article.converter;


import kr.apartribebackend.article.domain.CategoryCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;


@Slf4j
public class CategoryConverter implements Converter<String, CategoryCondition> {

    @Override
    public CategoryCondition convert(String source) {
        return null;
    }
}

