package kr.apartribebackend.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentCountRes;
import kr.apartribebackend.comment.dto.CommentResProjection;
import lombok.RequiredArgsConstructor;
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
    public CommentCountRes totalCountsForBoardComments(final Long memberId, final Long boardId) {
        return jpaQueryFactory
                .select(Projections.fields(CommentCountRes.class,
                        Wildcard.count.as("commentCount")))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .fetchOne();
    }

    @Override
    public List<CommentResProjection> findCommentsByBoardId(final Long memberId, final Long boardId) {
        final List<CommentResProjection> commentResProjections =
                jpaQueryFactory
                        .select(Projections.fields(CommentResProjection.class,
                                comment.parent.id.as("parentId"),
                                comment.id.as("commentId"),
                                comment.content.as("content"),
                                comment.liked.as("liked"),
                                member.profileImageUrl.as("profileImage"),
                                Expressions.as(
                                        JPAExpressions
                                                .select(commentLiked)
                                                .from(commentLiked)
                                                .where(
                                                        commentLiked.member.id.eq(memberId),
                                                        commentLiked.comment.id.eq(comment.id)
                                                )
                                                .exists(),
                                        "memberLiked"
                                ),
                                Expressions.as(
                                        JPAExpressions
                                                .select(comment)
                                                .from(comment)
                                                .where(member.id.eq(memberId))
                                                .exists(),
                                        "memberCreated"
                                ),
                                comment.children.size().as("childCounts"),
                                comment.createdAt.as("createdAt"),
                                comment.createdBy.as("createdBy")))
                        .from(comment)
                        .innerJoin(comment.member, member)
                        .leftJoin(comment.parent)
                        .where(comment.board.id.eq(boardId))
                        .orderBy(
                                comment.parent.id.asc().nullsFirst(),
                                comment.createdAt.desc()
                        )
                        .fetch();

        final List<CommentResProjection> commentResProjectionList = new ArrayList<>();
        final Map<Long, CommentResProjection> map = new HashMap<>();
        commentResProjections.forEach(c -> {
            map.put(c.getCommentId(), c);
            if (c.getParentId() != null) {
                map.get(c.getParentId()).getChildren().add(c);
            } else {
                commentResProjectionList.add(c);
            }
        });

        return commentResProjectionList;
    }

    @Override
    public List<BestCommentResponse> bestCommentRankViaLastWeek(final String apartCode) {
        return jpaQueryFactory
                .select(Projections.fields(
                        BestCommentResponse.class,
                        member.id.as("id"),
                        member.nickname.as("nickname"),
                        member.profileImageUrl.as("profileImage"),
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

    @Override
    public Optional<Comment> findCommentWithMemberAndApartmentForApartId(final String apartId,
                                                                         final Long boardId,
                                                                         final Long commentId) {
        final Comment result = jpaQueryFactory
                .selectFrom(comment)
                .innerJoin(comment.member, member).fetchJoin()
                .innerJoin(comment.board, board).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
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
