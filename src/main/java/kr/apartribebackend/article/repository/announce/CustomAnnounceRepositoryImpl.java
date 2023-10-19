package kr.apartribebackend.article.repository.announce;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import kr.apartribebackend.article.dto.announce.SingleAnnounceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QAnnounce.*;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomAnnounceRepositoryImpl implements CustomAnnounceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AnnounceResponse> findAnnouncesByLevel(final String apartId,
                                                       final Level level,
                                                       final Pageable pageable) {
        final List<Announce> announces = jpaQueryFactory
                .selectFrom(announce)
                .innerJoin(announce.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
                        apartment.code.eq(apartId),
                        levelCondition(level)
                )
                .fetch();

        final List<AnnounceResponse> contents = announces.stream().map(AnnounceResponse::from).toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(announce)
                .innerJoin(announce.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartment.code.eq(apartId),
                        levelCondition(level)
                );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public List<SingleAnnounceResponse> findJoinedAnnounceById(Long announceId) {
        final List<Announce> results = jpaQueryFactory
                .selectFrom(announce)
                .leftJoin(announce.comments, comment).fetchJoin()
                .where(announce.id.eq(announceId))
                .fetch();

        final List<SingleAnnounceResponse> collect = results.stream()
                .map(SingleAnnounceResponse::from).toList();
        return collect;
    }

    private BooleanExpression levelCondition(final Level level) {
        return level != Level.ALL ? announce.level.eq(level) : null;
    }

}
