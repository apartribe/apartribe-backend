package kr.apartribebackend.category.dto;

import jakarta.validation.constraints.NotEmpty;

public record TogetherCategoryAppendReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다.") String category
) {

    public TogetherCategoryDto toDto() {
        return TogetherCategoryDto.builder()
                .name(category)
                .build();
    }
}
