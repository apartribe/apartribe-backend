package kr.apartribebackend.category.repository;

import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.dto.CategoryListRes;

import java.util.List;
import java.util.Optional;

public interface CustomCategoryRepository {

    Optional<Category> findCategoryByTagAndNameWithApart(String apartId, String tag, String name);

    List<CategoryListRes> findCategoriesByTagWithApart(String apartId, String tag);

}
