package kr.apartribebackend.article.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.dto.ArticleInCommunityRes;
import kr.apartribebackend.article.dto.ArticleResponse;
import kr.apartribebackend.article.dto.Top5ArticleResponse;
import kr.apartribebackend.article.dto.together.SingleArticleResponseProjection;
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
import static kr.apartribebackend.likes.domain.QBoardLiked.*;
import static kr.apartribebackend.member.domain.QMember.*;
import static org.springframework.util.ObjectUtils.isEmpty;


@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArticleResponse> findArticlesByCategory(final String apartId,
                                                        final String categoryName,
                                                        final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        final List<Article> content = jpaQueryFactory
                .selectFrom(article)
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

        final List<ArticleResponse> articleResponses = content.stream()
                .map(article -> ArticleResponse.from(article, article.getMember())).toList();

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
     * 커뮤니티 게시글 단일 조회. (1) - 쿼리를 나눠서 실행
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
     * 커뮤니티 게시글 단일 조회. (2) - SubQuery 를 포함한 한방 쿼리
     * @param memberId
     * @param apartId
     * @param articleId
     * @return
     */
    @Override
    public Optional<SingleArticleResponseProjection> findArticleForApartId(final Long memberId,
                                                                           final String apartId,
                                                                           final Long articleId) {
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
                        member.profileImageUrl.as("profileImage"),
                        article.thumbnail.as("thumbnail"),
                        article.createdAt.as("createdAt"),
                        category.name.as("category"),
                        article.title.as("title"),
                        article.content.as("content"),
                        article.liked.as("liked"),
                        article.saw.as("saw")))
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


//package kr.apartribebackend.article.repository;
//
//import com.querydsl.core.types.Projections;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import kr.apartribebackend.article.domain.Article;
//import kr.apartribebackend.article.dto.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//
//import static kr.apartribebackend.article.domain.QArticle.*;
//import static kr.apartribebackend.comment.domain.QComment.*;
//
//@RequiredArgsConstructor
//public class CustomArticleRepositoryImpl implements CustomArticleRepository {
//
//    private final JPAQueryFactory jpaQueryFactory;
//
//    @Override
//    public List<Top5ArticleResponse> findTop5ArticleViaLiked() {
//        return jpaQueryFactory
//                .select(Projections.fields(Top5ArticleResponse.class,
//                        article.id.as("id"),
//                        article.title.as("title")))
//                .from(article)
//                .orderBy(article.liked.desc())
//                .limit(5)
//                .fetch();
//    }
//
//    @Override
//    public List<Top5ArticleResponse> findTop5ArticleViaView() {
//        return jpaQueryFactory
//                .select(Projections.fields(Top5ArticleResponse.class,
//                        article.id.as("id"),
//                        article.title.as("title")))
//                .from(article)
//                .orderBy(article.saw.desc())
//                .limit(5)
//                .fetch();
//    }
//
//    @Override
//    public List<ArticleInCommunityRes> searchArticleInCommunity(final String title) {
//        return jpaQueryFactory
//                .selectFrom(article)
//                .where(isTitleContainsIgnoreCase2(title))
//                .fetch()
//                .stream()
//                .map(ArticleInCommunityRes::from)
//                .toList();
//    }
//
//    @Override
//    public List<SingleArticleResponse> findJoinedArticleById(final Long articleId) {
//        final List<Article> results = jpaQueryFactory
//                .selectFrom(article)
//                .leftJoin(article.comments, comment).fetchJoin()
//                .where(article.id.eq(articleId))
//                .fetch();
//
//        final List<SingleArticleResponse> collect = results.stream()
//                .map(SingleArticleResponse::from).toList();
//        return collect;
//    }
//
//    private BooleanExpression isTitleEqIgnoreCase(final String title) {
//        return StringUtils.hasText(title) ? article.title.equalsIgnoreCase(title) : null;
//    }
//
//    private  BooleanExpression isTitleContainsIgnoreCase(final String title) {
//        if (!StringUtils.hasText(title))
//            return null;
//        return article.title.likeIgnoreCase(title + "%" )
//                .or(article.title.likeIgnoreCase("%" + title + "%"))
//                .or(article.title.likeIgnoreCase("%" + title))
//                .or(isTitleEqIgnoreCase(title));
//    }
//
//    private  BooleanExpression isTitleContainsIgnoreCase2(final String title) {
//        return StringUtils.hasText(title) ? article.title.containsIgnoreCase(title) : null;
//    }
//
//}
//
////    @Override
////    public List<SingleArticleResponse> findJoinedArticleById(Long articleId) {
////        List<Tuple> results = jpaQueryFactory
////                .select(
////                        article,
////                        JPAExpressions.select(Wildcard.count)
////                                .from(comment)
////                                .where(comment.article.id.eq(articleId)))
////                .from(article)
////                .leftJoin(article.comments, comment)
////                .where(article.id.eq(articleId))
////                .fetch();
////
////        List<SingleArticleResponse> result = results.stream()
////                .map(tuple -> new SingleArticleResponse(
////                        tuple.get(article),
////                        tuple.get(JPAExpressions.select(Wildcard.count)
////                                .from(comment)
////                                .where(comment.article.id.eq(articleId)))))
////                .collect(Collectors.toList());
////        return result;
////    }
//
////    @Override
////    public List<ArticleSingleResultResponse> findJoinedArticleById(Long articleId) {
////        List<ArticleSingleResultResponse> transform = jpaQueryFactory
////                .selectFrom(article)
////                .leftJoin(article.comments, comment)
////                .where(article.id.eq(articleId))
////                .transform(groupBy(article.id)
////                        .list(new QArticleSingleResultResponse(
////                                article.id,
////                                article.title,
////                                article.content,
////                                article.liked,
////                                article.saw,
////                                JPAExpressions.select(Wildcard.count)
////                                        .from(comment)
////                                        .where(comment.article.id.eq(articleId)),
////                                list(new QCommentSingleResultResponse(
////                                        comment.id,
////                                        comment.content,
////                                        comment.liked,
////                                        comment.createdBy
////                                ))
////                        )));
////        return transform;
////    }
//
////    @Override
////    public List<Article> findJoinedArticleById(Long articleId) {
////        List<Article> articles = jpaQueryFactory
////                .selectFrom(article)
////                .leftJoin(article.comments, comment)
////                .where(article.id.eq(articleId))
////                .fetch();
////
////        for (Article article : articles) {
////            article.getMember().getNickname();
////            List<Comment> comments = article.getComments();
////            for (Comment comment : comments) {
////                comment.getContent();
////            }
////        }
////        return articles;
////    }
//
////    @Override
////    public List<JoinedArticleResponse> findJoinedArticleById(Long articleId) {
////        return jpaQueryFactory
////                .selectFrom(article)
////                .innerJoin(article.member, member)
////                .innerJoin(member.comments, comment)
////                .where(article.id.eq(articleId))
////                .transform(groupBy(article.id)
////                        .list(new QJoinedArticleResponse(
////                                article.id,
////                                article.title,
////                                article.content,
////                                article.saw,
////                                article.liked,
////                                member.nickname,
////                                JPAExpressions.select(Wildcard.count)
////                                        .from(comment)
////                                        .where(comment.article.id.eq(articleId)),
////                                list(new QJoinedCommentResponse(
////                                        comment.createdBy,
////                                        comment.content,
////                                        comment.liked
////                                ))
////                        )));
////    }
//
////    @Override
////    public List<JoinedArticleResponse> findJoinedArticleById(Long articleId) {
////        return jpaQueryFactory
////                .selectFrom(article)
////                .innerJoin(article.member, member)
////                .innerJoin(member.comments, comment)
////                .where(article.id.eq(articleId))
////                .transform(groupBy(article.id)
////                        .list(
////                                Projections.constructor(
////                                        JoinedArticleResponse.class,
////                                        article.id.as("articleId"),
////                                        article.title.as("title"),
////                                        article.content.as("content"),
////                                        article.saw.as("saw"),
////                                        article.liked.as("liked"),
////                                        member.nickname.as("nickname"),
////                                        Expressions.asNumber(
////                                                ExpressionUtils.as(
////                                                        JPAExpressions.select(Wildcard.count)
////                                                                .from(comment)
////                                                                .where(comment.article.id.eq(articleId)),
////                                                        Expressions.numberPath(
////                                                                Long.class, "commentCounts"))),
////                                        list(Projections.constructor(
////                                                JoinedCommentResponse.class,
////                                                comment.createdBy.as("writtenBy"),
////                                                comment.content.as("value"),
////                                                comment.liked.as("likes"))))));
////    }
//
////    @Override
////    public List<JoinedArticleResponse> findJoinedArticleById(Long articleId) {
////        return jpaQueryFactory
////                .selectFrom(article)
////                .innerJoin(article.member, member)
////                .innerJoin(member.comments, comment)
////                .where(article.id.eq(articleId))
////                .transform(groupBy(article.id)
////                        .list(
////                                Projections.constructor(
////                                        JoinedArticleResponse.class,
////                                        article.id.as("articleId"),
////                                        article.title.as("title"),
////                                        article.content.as("content"),
////                                        article.saw.as("saw"),
////                                        article.liked.as("liked"),
////                                        member.nickname.as("nickname"),
////                                        list(Projections.constructor(
////                                                JoinedCommentResponse.class,
////                                                comment.createdBy.as("writtenBy"),
////                                                comment.content.as("value"),
////                                                comment.liked.as("likes"))))));
////    }
//
