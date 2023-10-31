package kr.apartribebackend.likes.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.likes.domain.CommentLiked;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.likes.domain.QCommentLiked.*;
import static kr.apartribebackend.member.domain.QMember.member;

@RequiredArgsConstructor
public class CustomCommentLikedRepositoryImpl implements CustomCommentLikedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<CommentLiked> findCommentLikedByMember(final Long memberId, final Long commentId) {
        final CommentLiked result = jpaQueryFactory
                .select(commentLiked)
                .from(commentLiked)
                .innerJoin(commentLiked.comment, comment)
                .innerJoin(commentLiked.member, member)
                .where(
                        member.id.eq(memberId),
                        comment.id.eq(commentId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Integer isMemberLikedToComment(Long memberId, Long commentId) {
        return jpaQueryFactory
                .selectOne()
                .from(commentLiked)
                .innerJoin(commentLiked.member, member)
                .innerJoin(commentLiked.comment, comment)
                .where(
                        member.id.eq(memberId),
                        comment.id.eq(commentId)
                )
                .fetchFirst();
    }

}
