package kr.apartribebackend.category.dto;

public record CategoryListRes(
        String categoryName
) {
    public static CategoryListRes from(ArticleCategoryDto articleCategoryDto) {
        return new CategoryListRes(articleCategoryDto.getName());
    }
}
