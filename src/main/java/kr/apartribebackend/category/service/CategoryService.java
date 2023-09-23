package kr.apartribebackend.category.service;

import kr.apartribebackend.category.dto.CategoryDto;
import kr.apartribebackend.category.exception.CategoryAlreadyExistsException;
import kr.apartribebackend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addCategory(CategoryDto categoryDto) {
        categoryRepository.findByName(categoryDto.getName())
                .ifPresentOrElse(category -> { throw new CategoryAlreadyExistsException(); },
                        () -> categoryRepository.save(categoryDto.toEntity()));
    }

}
