package kr.apartribebackend.category.repository;

import kr.apartribebackend.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository
        extends JpaRepository<Category, Long>, CustomCategoryRepository {

}
