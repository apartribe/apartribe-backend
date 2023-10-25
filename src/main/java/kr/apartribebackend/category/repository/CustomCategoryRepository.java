package kr.apartribebackend.category.repository;

import kr.apartribebackend.category.domain.Category;

import java.util.Optional;

public interface CustomCategoryRepository {

    Optional<Category> findCategoryByTagAndNameWithApart(String apartId, String tag, String name);

}
