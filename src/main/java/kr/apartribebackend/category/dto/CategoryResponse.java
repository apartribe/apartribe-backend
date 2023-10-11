package kr.apartribebackend.category.dto;

import kr.apartribebackend.category.domain.Category;


public record CategoryResponse(
        Long id,
        String name
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
