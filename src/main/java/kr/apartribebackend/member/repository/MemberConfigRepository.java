package kr.apartribebackend.member.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.domain.QApartment;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.QBoard;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.QMember;
import kr.apartribebackend.member.dto.MemberArticleRes;
import kr.apartribebackend.member.dto.MemberBoardResponse;
import kr.apartribebackend.member.dto.MemberCommentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.apart.domain.QApartment.apartment;
import static kr.apartribebackend.article.domain.QArticle.*;
import static kr.apartribebackend.article.domain.QBoard.board;
import static kr.apartribebackend.category.domain.QCategory.*;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.member.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberConfigRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 내가 쓴 댓글 --> 사실 email 이 uniq 해서 아파트 정보와 join 할 필요는 없지만, 연습삼아 해본것이다.
    public Page<MemberCommentRes> findCommentsForMember(final Member member,
                                                        final Apartment apartment,
                                                        final Pageable pageable) {
        List<MemberCommentRes> memberCommentRes = jpaQueryFactory
                .select(Projections.fields(
                        MemberCommentRes.class,
                        comment.id,
                        comment.content,
                        comment.createdBy,
                        comment.createdAt,
                        board.boardType,
                        board.id.as("boardId")))
                .from(comment)
                .innerJoin(comment.member, QMember.member)
                .innerJoin(comment.board, board)
                .innerJoin(QMember.member.apartment, QApartment.apartment)
                .where(
                        QApartment.apartment.code.eq(apartment.getCode()),
                        QApartment.apartment.name.eq(apartment.getName()),
                        QMember.member.email.eq(member.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(comment)
                .innerJoin(comment.member, QMember.member)
                .innerJoin(QMember.member.apartment, QApartment.apartment)
                .where(comment.member.id.eq(member.getId()));

        return PageableExecutionUtils.getPage(memberCommentRes, pageable, countQuery::fetchOne);
    }

    // 내가 쓴 글 --> 사실 email 이 uniq 해서 아파트정보와 join 은 필요없지만. 한번 해본 것이다.
    public Page<MemberBoardResponse> findArticlesForMember(final Member member,
                                                           final Apartment apartment,
                                                           final Pageable pageable) {
        List<Board> contents = jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.member, QMember.member).fetchJoin()
                .innerJoin(QMember.member.apartment, QApartment.apartment).fetchJoin()
                .where(
                        QApartment.apartment.code.eq(apartment.getCode()),
                        QApartment.apartment.name.eq(apartment.getName()),
                        QMember.member.email.eq(member.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        List<MemberBoardResponse> results = contents.stream().map(MemberBoardResponse::from).collect(Collectors.toList());

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(board)
                .innerJoin(board.member, QMember.member)
                .innerJoin(QMember.member.apartment, QApartment.apartment)
                .where(
                        QApartment.apartment.code.eq(apartment.getCode()),
                        QApartment.apartment.name.eq(apartment.getName()),
                        QMember.member.id.eq(member.getId())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

}

//    public Page<MemberArticleRes> findArticlesForMember(final Member member,
//                                                        final Apartment apartment,
//                                                        final Pageable pageable) {
//        List<MemberArticleRes> memberArticleRes = jpaQueryFactory
//                .select(Projections.fields(
//                        MemberArticleRes.class,
//                        article.id,
//                        category.name.as("category"),
//                        article.title,
//                        article.content,
//                        article.createdBy,
//                        article.createdAt))
//                .from(article)
//                .innerJoin(article.member, QMember.member)
//                .innerJoin(article.category, category)
//                .where(QMember.member.id.eq(member.getId()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = jpaQueryFactory
//                .select(Wildcard.count)
//                .from(article)
//                .innerJoin(article.member, QMember.member)
//                .where(QMember.member.id.eq(member.getId()));
//
//        return PageableExecutionUtils.getPage(memberArticleRes, pageable, countQuery::fetchOne);
//    }

//    public Page<MemberCommentRes> findCommentsForMember(final Member member,
//                                                        final Pageable pageable) {
//        List<MemberCommentRes> memberCommentRes = jpaQueryFactory
//                .select(Projections.fields(
//                        MemberCommentRes.class,
//                        comment.id,
//                        comment.content,
//                        comment.createdBy,
//                        comment.createdAt))
//                .from(comment)
//                .innerJoin(comment.member, QMember.member)
//                .where(QMember.member.id.eq(member.getId()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = jpaQueryFactory
//                .select(Wildcard.count)
//                .from(comment)
//                .where(comment.member.id.eq(member.getId()));
//
//        return PageableExecutionUtils.getPage(memberCommentRes, pageable, countQuery::fetchOne);
//    }

