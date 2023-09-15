package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends
        JpaRepository<Article, Long>, CustomArticleRepository {

    Page<Article> findArticlesByCategory(Category category, Pageable pageable);

}