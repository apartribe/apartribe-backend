package kr.apartribebackend.category.repository;

import kr.apartribebackend.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByTagAndName(String tag, String name);

    List<Category> findCategoriesByTag(String tag);

    boolean existsByName(String name);

}
