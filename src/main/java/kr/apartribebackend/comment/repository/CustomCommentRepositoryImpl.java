package kr.apartribebackend.comment.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentRes;
import kr.apartribebackend.likes.dto.CommentLikedWithCommentAndMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QBoard.board;
import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.likes.domain.QCommentLiked.*;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentRes> findCommentsByBoardId(final Long memberId,
                                                  final Long boardId,
                                                  final Pageable pageable) {
        final List<Comment> commentLists = jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.parent).fetchJoin()
                .where(comment.board.id.eq(boardId))
                .orderBy(comment.parent.id.asc().nullsFirst(),
                        comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> commentIds = commentLists.stream().map(Comment::getId).toList();
        List<CommentLikedWithCommentAndMember> commentLikedWithCommentAndMembers = jpaQueryFactory
                .select(Projections.fields(CommentLikedWithCommentAndMember.class,
                        comment.id.as("commentId"),
                        member.id.as("memberId")))
                .from(commentLiked)
                .innerJoin(commentLiked.comment, comment)
                .innerJoin(commentLiked.member, member)
                .where(
                        comment.id.in(commentIds),
                        member.id.eq(memberId)
                )
                .fetch();

        List<CommentRes> commentResList = new ArrayList<>();
        Map<Long, CommentRes> map = new HashMap<>();
        commentLists.forEach(c -> {
            CommentRes commentRes = CommentRes.from(c, commentLikedWithCommentAndMembers);
            map.put(commentRes.getId(), commentRes);
            if (c.getParent() != null) {
                map.get(c.getParent().getId()).getChildren().add(commentRes);
            } else {
                commentResList.add(commentRes);
            }
        });

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(comment)
                .leftJoin(comment.parent)
                .where(comment.board.id.eq(boardId));

        return PageableExecutionUtils.getPage(commentResList, pageable, countQuery::fetchOne);
    }

    @Override
    public List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode) {
        return jpaQueryFactory
                .select(Projections.fields(
                        BestCommentResponse.class,
                        member.id.as("id"),
                        member.nickname.as("nickname"),
                        comment.count().as("commentCount")))
                .from(comment)
                .innerJoin(comment.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        comment.createdAt.after(LocalDateTime.now().minusDays(7)),
                        apartment.code.eq(apartCode)
                )
                .groupBy(member.nickname).orderBy(comment.count().desc())
                .fetch();
    }

    @Override
    public Optional<Comment> findCommentForApartId(final String apartId,
                                                   final Long boardId,
                                                   final Long commentId) {
        final Comment result = jpaQueryFactory
                .selectFrom(comment)
                .innerJoin(comment.member, member)
                .innerJoin(comment.board, board)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        board.id.eq(boardId),
                        comment.id.eq(commentId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }

}

//    @Override
//    public Page<CommentRes> findCommentsByBoardId(final Long boardId,
//                                                  final Pageable pageable) {
//        final List<Comment> commentLists = jpaQueryFactory
//                .selectFrom(comment)
//                .leftJoin(comment.parent).fetchJoin()
//                .where(comment.board.id.eq(boardId))
//                .orderBy(comment.parent.id.asc().nullsFirst(),
//                        comment.createdAt.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        List<CommentRes> commentResList = new ArrayList<>();
//        Map<Long, CommentRes> map = new HashMap<>();
//        commentLists.forEach(c -> {
//            CommentRes commentRes = CommentRes.from(c);
//            map.put(commentRes.getId(), commentRes);
//            if (c.getParent() != null) {
//                map.get(c.getParent().getId()).getChildren().add(commentRes);
//            } else {
//                commentResList.add(commentRes);
//            }
//        });
//
//        final JPAQuery<Long> countQuery = jpaQueryFactory
//                .select(Wildcard.count)
//                .from(comment)
//                .leftJoin(comment.parent)
//                .where(comment.board.id.eq(boardId));
//
//        return PageableExecutionUtils.getPage(commentResList, pageable, countQuery::fetchOne);
//    }

//        List<CommentLiked> memberCommentLikedList = jpaQueryFactory
//                .selectFrom(commentLiked)
//                .innerJoin(commentLiked.comment, comment)
//                .innerJoin(commentLiked.member, member)
//                .where(
//                        comment.id.in(commentIds),
//                        member.id.eq(memberId)
//                )
//                .fetch();