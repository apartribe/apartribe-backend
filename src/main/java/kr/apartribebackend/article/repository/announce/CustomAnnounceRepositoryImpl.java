package kr.apartribebackend.article.repository.announce;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.AnnounceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QAnnounce.*;
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
                        apartmentCondition(apartId),
                        levelCondition(level)
                )
                .fetch();

        final List<AnnounceResponse> contents = announces.stream()
                .map(announce -> AnnounceResponse.from(announce, announce.getMember())).toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(announce)
                .innerJoin(announce.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        levelCondition(level)
                );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Announce> findAnnounceForApartId(final String apartId, final Long announceId) {
        final Announce result = jpaQueryFactory
                .selectFrom(announce)
                .innerJoin(announce.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment).fetchJoin()
                .where(
                        apartmentCondition(apartId),
                        announce.id.eq(announceId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }


    private BooleanExpression levelCondition(final Level level) {
        return level != Level.ALL ? announce.level.eq(level) : null;
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }

}
