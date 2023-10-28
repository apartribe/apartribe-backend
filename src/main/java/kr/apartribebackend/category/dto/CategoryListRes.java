package kr.apartribebackend.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter @AllArgsConstructor
public class CategoryListRes {

    private String categoryName;

    public static CategoryListRes from(ArticleCategoryDto articleCategoryDto) {
        return new CategoryListRes(articleCategoryDto.getName());
    }
}
