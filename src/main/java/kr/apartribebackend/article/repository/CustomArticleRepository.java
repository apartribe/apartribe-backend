package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.dto.*;

import java.util.List;

public interface CustomArticleRepository {

    List<Top5ArticleResponse> findTop5ArticleViaLiked();

    List<SingleArticleResponse> findJoinedArticleById(Long articleId);

    List<Top5ArticleResponse> findTop5ArticleViaView();

}
