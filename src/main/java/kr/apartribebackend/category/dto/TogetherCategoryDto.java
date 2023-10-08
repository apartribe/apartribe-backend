package kr.apartribebackend.category.dto;

import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.domain.TogetherCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TogetherCategoryDto {

    private Long id;
    private String name;

    @Builder
    private TogetherCategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TogetherCategoryDto from(Category category) {
        return TogetherCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public TogetherCategory toEntity() {
        return TogetherCategory.builder()
                .id(id)
                .name(name)
                .build();
    }

}
