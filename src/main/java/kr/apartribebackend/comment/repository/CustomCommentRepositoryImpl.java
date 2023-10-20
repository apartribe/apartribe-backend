package kr.apartribebackend.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.apart.domain.QApartment;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import kr.apartribebackend.comment.dto.CommentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentRes> findCommentsByBoardId(final Long boardId,
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
        final List<CommentRes> commentRes = commentLists.stream()
                .map(CommentRes::from)
                .toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .leftJoin(comment.parent)
                .where(comment.board.id.eq(boardId));

        return PageableExecutionUtils.getPage(commentRes, pageable, countQuery::fetchOne);
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

}
