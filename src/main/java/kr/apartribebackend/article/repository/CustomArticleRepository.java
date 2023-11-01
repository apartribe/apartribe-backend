package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.dto.together.SingleArticleResponseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CustomArticleRepository {

    Page<ArticleResponse> findArticlesByCategory(String apartId, String categoryName, Pageable pageable);

    List<Top5ArticleResponse> findTop5ArticleViaLiked(String apartId);

    List<Top5ArticleResponse> findTop5ArticleViaView(String apartId);

    List<ArticleInCommunityRes> searchArticleInCommunity(String apartId, String title);

    Optional<Article> findArticleForApartId(String apartId, Long articleId);

    Optional<SingleArticleResponseProjection> findArticleForApartId(Long memberId, String apartId, Long articleId);

}
