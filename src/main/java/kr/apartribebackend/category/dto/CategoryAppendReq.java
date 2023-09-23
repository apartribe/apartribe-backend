package kr.apartribebackend.category.dto;

import jakarta.validation.constraints.NotEmpty;

public record CategoryAppendReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다.") String category
) {

    public CategoryDto toDto() {
        return CategoryDto.builder()
                .name(category)
                .build();
    }
}