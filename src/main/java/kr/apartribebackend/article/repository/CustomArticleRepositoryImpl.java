package kr.apartribebackend.article.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.dto.*;
import kr.apartribebackend.global.utils.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QArticle.*;
import static kr.apartribebackend.article.domain.QBoard.*;
import static kr.apartribebackend.category.domain.QCategory.category;
import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.likes.domain.QBoardLiked.*;
import static kr.apartribebackend.member.domain.QMember.*;
import static org.springframework.util.ObjectUtils.isEmpty;


@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext EntityManager entityManager;

    @Override
    public Page<ArticleResponse> findArticlesByCategory(final String apartId,
                                                        final String categoryName,
                                                        final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<ArticleResponse> articleResponses = jpaQueryFactory
                .select(
                        new QArticleResponse(
                                article.id,
                                article.liked,
                                article.saw,
                                article.title,
                                article.content,
                                article.thumbnail,
                                JPAExpressions
                                        .select(Wildcard.count)
                                        .from(comment)
                                        .where(comment.board.id.eq(article.id)),
                                article.createdAt,
                                article.createdBy,
                                member.profileImageUrl,
                                article.onlyApartUser
                        )
                )
                .from(article)
                .innerJoin(article.member, member)
                .innerJoin(article.category, category)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        categoryNameEq(categoryName)
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(article)
                .innerJoin(article.member, member)
                .innerJoin(article.category, category)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        categoryNameEq(categoryName)
                );

        return PageableExecutionUtils.getPage(articleResponses, pageable, countQuery::fetchOne);
    }

    /**
     * 커뮤니티 게시글 단일 조회 (1) - 쿼리를 나눠서 세번 실행
     * 1. 게시글의 조회수 를 1 증가시키는 쿼리
     * 2. SingleArticleResponse 가 조회되는 쿼리
     * 3. 게시글에 좋아요가 달려있는지 확인하는 쿼리
     * @param apartId
     * @param articleId
     * @return
     */
    @Override
    public Optional<Article> findArticleForApartId(final String apartId, final Long articleId) {
        final Article result = jpaQueryFactory
                .selectFrom(article)
                .innerJoin(article.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        article.id.eq(articleId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    /**
     * 커뮤니티 게시글 단일 조회 (2) - SubQuery(좋아요 여부, 게시글 작성자 일치여부) + BulkQuery(조회수 증가) 를 이용한 한방쿼리 + apartCode 정보
     * @param memberId
     * @param apartId
     * @param articleId
     * @return
     */
    @Override
    public Optional<SingleArticleResponseProjection> findAnnounceForApartId(final Long memberId,
                                                                            final String apartId,
                                                                            final Long articleId) {
        jpaQueryFactory.update(article)
                .set(article.saw, article.saw.add(1))
                .where(article.id.eq(articleId))
                .execute();

        entityManager.clear();

        final SingleArticleResponseProjection singleArticleResponseProjection = jpaQueryFactory
                .select(Projections.fields(SingleArticleResponseProjection.class,
                        article.id.as("id"),
                        article.createdBy.as("createdBy"),
                        Expressions.as(
                                JPAExpressions.select(article)
                                        .from(article)
                                        .where(
                                                article.id.eq(articleId),
                                                article.member.id.eq(memberId)
                                        )
                                        .exists(),
                                "memberCreated"
                        ),
                        Expressions.as(
                                JPAExpressions.select(boardLiked)
                                        .from(boardLiked)
                                        .where(
                                                boardLiked.member.id.eq(memberId),
                                                boardLiked.board.id.eq(articleId)
                                        )
                                        .exists(),
                                "memberLiked"
                        ),
                        article.onlyApartUser.as("onlyApartUser"),
                        member.profileImageUrl.as("profileImage"),
                        article.thumbnail.as("thumbnail"),
                        article.createdAt.as("createdAt"),
                        category.name.as("category"),
                        article.title.as("title"),
                        article.content.as("content"),
                        article.liked.as("liked"),
                        article.saw.as("saw"),
                        apartment.code.as("apartCode")))
                .from(article)
                .innerJoin(article.member, member)
                .innerJoin(article.category, category)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        article.id.eq(articleId)
                )
                .fetchOne();

        return Optional.ofNullable(singleArticleResponseProjection);
    }

    @Override
    public List<Top5ArticleResponse> findTop5ArticleViaLiked(final String apartId) {
        return jpaQueryFactory
                .select(Projections.fields(Top5ArticleResponse.class,
                        board.id.as("id"),
                        board.boardType.as("boardType"),
                        board.title.as("title")))
                .from(board)
                .innerJoin(board.member, member)
                .innerJoin(member.apartment, apartment)
                .where(apartmentCondition(apartId))
                .orderBy(board.liked.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Top5ArticleResponse> findTop5ArticleViaView(final String apartId) {
        return jpaQueryFactory
                .select(Projections.fields(Top5ArticleResponse.class,
                        board.id.as("id"),
                        board.boardType.as("boardType"),
                        board.title.as("title")))
                .from(board)
                .innerJoin(board.member, member)
                .innerJoin(member.apartment, apartment)
                .where(apartmentCondition(apartId))
                .orderBy(board.saw.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<ArticleInCommunityRes> searchArticleInCommunity(final String apartId, final String title) {
        return jpaQueryFactory
                .select(Projections.fields(
                        ArticleInCommunityRes.class,
                        board.id.as("id"),
                        board.boardType.as("boardType"),
                        board.title.as("title")))
                .from(board)
                .innerJoin(board.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        isTitleContainsIgnoreCase2(title)
                )
                .orderBy(board.createdAt.desc())
                .fetch();
    }

    private BooleanExpression categoryNameEq(String categoryName) {
        return StringUtils.hasText(categoryName) ? category.name.eq(categoryName) : null;
    }

    private  BooleanExpression isTitleContainsIgnoreCase(final String title) {
        if (!StringUtils.hasText(title))
            return null;
        return board.title.likeIgnoreCase(title + "%" )
                .or(board.title.likeIgnoreCase("%" + title + "%"))
                .or(board.title.likeIgnoreCase("%" + title))
                .or(isTitleEqIgnoreCase(title));
    }

    private BooleanExpression isTitleEqIgnoreCase(final String title) {
        return StringUtils.hasText(title) ? board.title.equalsIgnoreCase(title) : null;
    }

    private  BooleanExpression isTitleContainsIgnoreCase2(final String title) {
        return StringUtils.hasText(title) ? board.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "saw":
                        OrderSpecifier<?> saw = QueryDslUtil.getSortedColumn(direction, article, "saw");
                        ORDERS.add(saw);
                        break;
                    case "liked":
                        OrderSpecifier<?> liked = QueryDslUtil.getSortedColumn(direction, article, "liked");
                        ORDERS.add(liked);
                        break;
                    default:
                        OrderSpecifier<?> createdAt = QueryDslUtil.getSortedColumn(direction, article, "createdAt");
                        ORDERS.add(createdAt);
                        break;
                }
            }
        }
        return ORDERS;
    }

}
