package kr.apartribebackend.category.dto;

public record CategoryListRes(
        String categoryName
) {
    public static CategoryListRes from(CategoryDto categoryDto) {
        return new CategoryListRes(categoryDto.getName());
    }
}
