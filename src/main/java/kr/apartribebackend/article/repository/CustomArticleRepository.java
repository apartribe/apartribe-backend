package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;

import java.util.List;

public interface CustomArticleRepository {

    List<Article> findTop5ArticleViaLiked();

}
