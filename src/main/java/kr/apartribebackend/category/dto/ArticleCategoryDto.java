package kr.apartribebackend.category.dto;

import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleCategoryDto {

    private Long id;
    private String name;

    @Builder
    private ArticleCategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ArticleCategoryDto from(Category category) {
        return ArticleCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public ArticleCategory toEntity() {
        return ArticleCategory.builder()
                .id(id)
                .name(name)
                .build();
    }

}


//package kr.apartribebackend.category.dto;
//
//import kr.apartribebackend.category.domain.Category;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class CategoryDto {
//
//    private Long id;
//    private String name;
//
//    @Builder
//    private CategoryDto(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//
//    public static CategoryDto from(Category category) {
//        return CategoryDto.builder()
//                .id(category.getId())
//                .name(category.getName())
//                .build();
//    }
//
//    public Category toEntity() {
//        return Category.builder()
//                .id(id)
//                .name(name)
//                .build();
//    }
//
//}
