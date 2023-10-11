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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category addArticleCategory(final String categoryTag,
                                   final ArticleCategoryDto articleCategoryDto) {
        return addCategory(categoryTag, articleCategoryDto.toEntity());
    }

    @Transactional
    public Category addTogetherCategory(final String categoryTag,
                                    final TogetherCategoryDto togetherCategoryDto) {
        return addCategory(categoryTag, togetherCategoryDto.toEntity());
    }

    public List<ArticleCategoryDto> listArticleCategory() {
        return listCategory(ARTICLE);
    }

    public List<ArticleCategoryDto> listTogetherCategory() {
        return listCategory(TOGETHER);
    }

    public Category addCategory(final String categoryTag,
                            final Category category) {
        categoryRepository.findCategoryByTagAndName(categoryTag, category.getName())
                .ifPresent(c -> { throw new CategoryAlreadyExistsException(); });
        return categoryRepository.save(category);
    }

    public List<ArticleCategoryDto> listCategory(final String categoryTag) {
        return categoryRepository.findCategoriesByTag(categoryTag).stream()
                .map(ArticleCategoryDto::from)
                .toList();
    }

}
