package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends
        JpaRepository<Article, Long>, CustomArticleRepository {

}
