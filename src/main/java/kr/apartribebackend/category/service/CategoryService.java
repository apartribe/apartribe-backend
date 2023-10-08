package kr.apartribebackend.category.service;

import kr.apartribebackend.category.dto.ArticleCategoryDto;
import kr.apartribebackend.category.exception.CategoryAlreadyExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addArticleCategory(final ArticleCategoryDto articleCategoryDto) {
        categoryRepository.findByName(articleCategoryDto.getName())
                .ifPresentOrElse(category -> { throw new CategoryAlreadyExistsException(); },
                        () -> categoryRepository.save(articleCategoryDto.toEntity()));
    }

    public List<ArticleCategoryDto> listArticleCategory() {
        return categoryRepository.findAll().stream()
                .map(ArticleCategoryDto::from)
                .toList();
    }
}
