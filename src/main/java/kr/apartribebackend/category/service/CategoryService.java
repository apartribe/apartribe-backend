package kr.apartribebackend.category.service;

import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.dto.ArticleCategoryDto;
import kr.apartribebackend.category.dto.TogetherCategoryDto;
import kr.apartribebackend.category.exception.CategoryAlreadyExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.apartribebackend.category.domain.CategoryTag.*;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addArticleCategory(final String categoryTag,
                                   final ArticleCategoryDto articleCategoryDto) {
        addCategory(categoryTag, articleCategoryDto.toEntity());
    }

    @Transactional
    public void addTogetherCategory(final String categoryTag,
                                    final TogetherCategoryDto togetherCategoryDto) {
        addCategory(categoryTag, togetherCategoryDto.toEntity());
    }

    public List<ArticleCategoryDto> listArticleCategory() {
        return categoryRepository.findCategoriesByTag(ARTICLE).stream()
                .map(ArticleCategoryDto::from)
                .toList();
    }

    public void addCategory(final String categoryTag,
                            final Category category) {
        categoryRepository.findCategoryByTagAndName(categoryTag, category.getName())
                .ifPresentOrElse(c -> { throw new CategoryAlreadyExistsException(); },
                        () -> categoryRepository.save(category));
    }

}
