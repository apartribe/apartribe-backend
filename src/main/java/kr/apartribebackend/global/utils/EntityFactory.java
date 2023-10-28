package kr.apartribebackend.global.utils;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.category.domain.ArticleCategory;
import kr.apartribebackend.category.domain.TogetherCategory;


public class EntityFactory {

    public static ArticleCategory.ArticleCategoryBuilder<?, ?> createArticleCategory(Apartment apartment, String categoryName) {
        return ArticleCategory.builder()
                .apartment(apartment)
                .name(categoryName);
    }

    public static TogetherCategory.TogetherCategoryBuilder<?, ?> createTogetherCategory(Apartment apartment, String categoryName) {
        return TogetherCategory.builder()
                .apartment(apartment)
                .name(categoryName);
    }

}
