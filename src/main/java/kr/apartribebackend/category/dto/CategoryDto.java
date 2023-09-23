package kr.apartribebackend.category.dto;

import kr.apartribebackend.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryDto {

    private Long id;
    private String name;

    @Builder
    private CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category toEntity() {
        return Category.builder()
                .id(id)
                .name(name)
                .build();
    }

}
