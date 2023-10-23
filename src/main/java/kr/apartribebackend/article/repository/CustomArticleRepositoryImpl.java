package kr.apartribebackend.article.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.apart.domain.QApartment;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.QBoard;
import kr.apartribebackend.article.dto.ArticleInCommunityRes;
import kr.apartribebackend.article.dto.ArticleResponse;
import kr.apartribebackend.article.dto.SingleArticleResponse;
import kr.apartribebackend.article.dto.Top5ArticleResponse;
import kr.apartribebackend.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QArticle.*;
import static kr.apartribebackend.article.domain.QBoard.*;
import static kr.apartribebackend.category.domain.QCategory.category;
import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.member.domain.QMember.*;


@RequiredArgsConstructor
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArticleResponse> findArticlesByCategory(final String categoryName,
                                                        final Pageable pageable) {
        final List<Article> content = jpaQueryFactory
                .selectFrom(article)
                .innerJoin(article.category, category)
                .where(categoryNameEq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final List<ArticleResponse> articleResponses = content.stream().map(ArticleResponse::from).toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(article.count())
                .from(article)
                .innerJoin(article.category, category)
                .where(categoryNameEq(categoryName));

        return PageableExecutionUtils.getPage(articleResponses, pageable, countQuery::fetchOne);
    }

    @Override
    public List<SingleArticleResponse> findJoinedArticleById(final Long articleId) {
        final List<Article> results = jpaQueryFactory
                .selectFrom(article)
                .leftJoin(article.comments, comment).fetchJoin()
                .where(article.id.eq(articleId))
                .fetch();

        final List<SingleArticleResponse> collect = results.stream()
                .map(SingleArticleResponse::from).toList();
        return collect;
    }

    @Override
    public List<Top5ArticleResponse> findTop5ArticleViaLiked() {
        return jpaQueryFactory
                .select(Projections.fields(Top5ArticleResponse.class,
                        article.id.as("id"),
                        article.title.as("title")))
                .from(article)
                .orderBy(article.liked.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<Top5ArticleResponse> findTop5ArticleViaView() {
        return jpaQueryFactory
                .select(Projections.fields(Top5ArticleResponse.class,
                        article.id.as("id"),
                        article.title.as("title")))
                .from(article)
                .orderBy(article.saw.desc())
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
