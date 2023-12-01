package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.article.dto.SingleArticleResponseProjection;
import kr.apartribebackend.article.exception.ArticleNotFoundException;
import kr.apartribebackend.article.exception.NotApartUserBoardException;
import kr.apartribebackend.member.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CustomArticleRepository {

    Page<ArticleResponse> findArticlesByCategory(String apartId, String categoryName, Pageable pageable);

    List<Top5ArticleResponse> findTop5ArticleViaLiked(String apartId);

    List<Top5ArticleResponse> findTop5ArticleViaView(String apartId);

    List<ArticleInCommunityRes> searchArticleInCommunity(String apartId, String title);

    /**
     * 커뮤니티 게시글 단일 조회 (1) - 쿼리를 나눠서 세번 실행
     * 1. 게시글의 조회수 를 1 증가시키는 쿼리
     * 2. SingleArticleResponse 가 조회되는 쿼리
     * 3. 게시글에 좋아요가 달려있는지 확인하는 쿼리
     * @param apartId
     * @param articleId
     * @return
     */
    Optional<Article> findArticleForApartId(String apartId, Long articleId);

    /**
     * 커뮤니티 게시글 단일 조회 (2) - SubQuery(좋아요 여부, 게시글 작성자 일치여부) + BulkQuery(조회수 증가) 를 이용한 한방쿼리 + apartCode 정보
     * @param memberId
     * @param apartId
     * @param articleId
     * @return
     */
    Optional<SingleArticleResponseProjection> findAnnounceForApartId(Long memberId, String apartId, Long articleId);

    /**
     * 커뮤니티 게시글 단일 조회 (2) 을 이용한 단일 게시글 조회 로직. 해당 메서드에서 조회된 게시글이 아파트전용 게시글인지 아닌지 검증하는 로직이 포함되어 있다.
     * @param memberDto
     * @param apartId
     * @param articleId
     * @return
     */
    default SingleArticleResponseProjection findArticleWithApartCodeForApartId(MemberDto memberDto, String apartId, Long articleId) {
        final SingleArticleResponseProjection singleArticleResponseProjection =
                findAnnounceForApartId(memberDto.getId(), apartId, articleId).orElseThrow(ArticleNotFoundException::new);
        if (singleArticleResponseProjection.isOnlyApartUser()) {
            if (!memberDto.getApartmentDto().getCode().equals(singleArticleResponseProjection.getApartCode())) {
                throw new NotApartUserBoardException();
            }
        }
        return singleArticleResponseProjection;
    }

}
