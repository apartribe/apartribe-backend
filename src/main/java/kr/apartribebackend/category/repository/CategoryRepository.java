package kr.apartribebackend.category.repository;

import kr.apartribebackend.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long>, CustomCategoryRepository {

    Optional<Category> findCategoryByTagAndName(String tag, String name);

}
