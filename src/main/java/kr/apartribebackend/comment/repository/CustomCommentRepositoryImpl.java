package kr.apartribebackend.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.comment.dto.BestCommentResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static kr.apartribebackend.comment.domain.QComment.*;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BestCommentResponse> bestCommentRankViaLastWeek() {
        return jpaQueryFactory
                .select(Projections.fields(
                        BestCommentResponse.class,
                        member.nickname.as("nickname"),
                        comment.count().as("commentCount")))
                .from(comment)
                .innerJoin(comment.member, member)
                .where(comment.createdAt.after(LocalDateTime.now().minusDays(7)))
                .groupBy(member.nickname)
                .fetch();
    }
}
