package kr.apartribebackend.article.repository.announce;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.dto.announce.*;
import kr.apartribebackend.global.utils.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QAnnounce.*;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.likes.domain.QBoardLiked.boardLiked;
import static kr.apartribebackend.member.domain.QMember.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CustomAnnounceRepositoryImpl implements CustomAnnounceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext EntityManager entityManager;

    @Override
    public Page<AnnounceResponse> findAnnouncesByLevel(final String apartId,
                                                       final Level level,
                                                       final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        final List<AnnounceResponse> contents = jpaQueryFactory
                .select(
                        new QAnnounceResponse(
                                announce.id,
                                announce.level,
                                announce.liked,
                                announce.saw,
                                announce.title,
                                announce.content,
                                announce.thumbnail,
                                JPAExpressions
                                        .select(Wildcard.count)
                                        .from(comment)
                                        .where(comment.board.id.eq(announce.id)),
                                announce.createdAt,
                                announce.createdBy,
                                member.profileImageUrl,
                                announce.onlyApartUser
                        )
                )
                .from(announce)
                .innerJoin(announce.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        levelCondition(level)
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

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
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        announce.id.eq(announceId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SingleAnnounceResponseProjection> findAnnounceForApartId(final Long memberId,
                                                                             final String apartId,
                                                                             final Long announceId) {
        jpaQueryFactory.update(announce)
                .set(announce.saw, announce.saw.add(1))
                .where(announce.id.eq(announceId))
                .execute();

        entityManager.clear();

        final SingleAnnounceResponseProjection singleAnnounceResponseProjection = jpaQueryFactory
                .select(new QSingleAnnounceResponseProjection(
                        announce.id,
                        announce.createdBy,
                        member.profileImageUrl,
                        announce.thumbnail,
                        announce.createdAt,
                        announce.level,
                        announce.title,
                        announce.content,
                        announce.floatFrom,
                        announce.floatTo,
                        announce.liked,
                        JPAExpressions.select(boardLiked)
                                .from(boardLiked)
                                .where(
                                        boardLiked.member.id.eq(memberId),
                                        boardLiked.board.id.eq(announceId)
                                )
                                .exists(),
                        JPAExpressions.select(announce)
                                .from(announce)
                                .where(
                                        announce.id.eq(announceId),
                                        announce.member.id.eq(memberId)
                                )
                                .exists(),
                        announce.onlyApartUser,
                        announce.saw,
                        member.position,
                        apartment.code))
                .from(announce)
                .innerJoin(announce.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        announce.id.eq(announceId)
                )
                .fetchOne();

        return Optional.ofNullable(singleAnnounceResponseProjection);
    }

    @Override
    public List<AnnounceWidgetRes> findWidgetValues(final String apartId) {
        final List<Announce> announces = jpaQueryFactory
                .selectFrom(announce)
                .innerJoin(announce.member, member)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        announce.floatFrom.loe(LocalDate.now()),
                        announce.floatTo.goe(LocalDate.now())
                )
                .fetch();

        return announces.stream().map(AnnounceWidgetRes::from).collect(Collectors.toList());
    }


    private BooleanExpression levelCondition(final Level level) {
        return level != Level.ALL ? announce.level.eq(level) : null;
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
                        OrderSpecifier<?> saw = QueryDslUtil.getSortedColumn(direction, announce, "saw");
                        ORDERS.add(saw);
                        break;
                    case "liked":
                        OrderSpecifier<?> liked = QueryDslUtil.getSortedColumn(direction, announce, "liked");
                        ORDERS.add(liked);
                        break;
                    default:
                        OrderSpecifier<?> createdAt = QueryDslUtil.getSortedColumn(direction, announce, "createdAt");
                        ORDERS.add(createdAt);
                        break;
                }
            }
        }

        return ORDERS;
    }

    public BooleanExpression isDateRangeWithin(final LocalDate floatFrom, final LocalDate floatTo) {
        return announce.floatFrom.between(floatFrom, floatTo)
                .or(announce.floatTo.between(floatFrom, floatTo))
                .or(announce.floatFrom.before(floatFrom).and(announce.floatTo.after(floatTo)));
    }

//    private OrderSpecifier[] createOrderSpecifier(Pageable pageable) {
//        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
//
//        orderSpecifiers.add(new OrderSpecifier(Order.DESC, person.name));
//        orderSpecifiers.add(new OrderSpecifier(Order.DESC, person.region));
//
//        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
//    }


//    @Override
//    public Page<AnnounceResponse> findAnnouncesByLevel(final String apartId,
//                                                       final Level level,
//                                                       final Pageable pageable) {
//        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
//
//        final List<Announce> announces = jpaQueryFactory
//                .selectFrom(announce)
//                .innerJoin(announce.member, member).fetchJoin()
//                .innerJoin(member.apartment, apartment).fetchJoin()
//                .where(
//                        apartmentCondition(apartId),
//                        levelCondition(level)
//                )
//                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        final List<AnnounceResponse> contents = announces.stream()
//                .map(announce -> AnnounceResponse.from(announce, announce.getMember())).toList();
//
//        final JPAQuery<Long> countQuery = jpaQueryFactory
//                .select(Wildcard.count)
//                .from(announce)
//                .innerJoin(announce.member, member)
//                .innerJoin(member.apartment, apartment)
//                .where(
//                        apartmentCondition(apartId),
//                        levelCondition(level)
//                );
//
//        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
//    }

}
