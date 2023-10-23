package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CustomArticleRepository {

    Page<ArticleResponse> findArticlesByCategory(String apartId, String categoryName, Pageable pageable);

    List<Top5ArticleResponse> findTop5ArticleViaLiked(final String apartId);

    List<Top5ArticleResponse> findTop5ArticleViaView(String apartId);

    List<ArticleInCommunityRes> searchArticleInCommunity(String apartId, String title);

    Optional<Article> findArticleForApartId(String apartId, Long articleId);
}


//package kr.apartribebackend.article.repository;
//
//import kr.apartribebackend.article.dto.*;
//
//import java.util.List;
//
//public interface CustomArticleRepository {
//
//    List<Top5ArticleResponse> findTop5ArticleViaLiked();
//
//    List<SingleArticleResponse> findJoinedArticleById(Long articleId);
//
//    List<Top5ArticleResponse> findTop5ArticleViaView();
//
//    List<ArticleInCommunityRes> searchArticleInCommunity(String title);
//}
