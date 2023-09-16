package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.dto.Top5ArticleResponse;

import java.util.List;

public interface CustomArticleRepository {

    List<Top5ArticleResponse> findTop5ArticleViaLiked();

}
