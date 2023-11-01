package kr.apartribebackend.article.repository.together;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.apartribebackend.article.domain.RecruitStatus;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.QSingleTogetherResponseProjection;
import kr.apartribebackend.article.dto.together.SingleTogetherResponseProjection;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import kr.apartribebackend.global.utils.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.apartment;
import static kr.apartribebackend.article.domain.QTogether.*;
import static kr.apartribebackend.category.domain.QCategory.category;
import static kr.apartribebackend.likes.domain.QBoardLiked.boardLiked;
import static kr.apartribebackend.member.domain.QMember.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class CustomTogetherRepositoryImpl implements CustomTogetherRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @PersistenceContext EntityManager entityManager;

    @Override
    public Page<TogetherResponse> findMultipleTogethersByCategory(final String apartId,
                                                                  final String categoryName,
                                                                  final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        final List<Together> content = jpaQueryFactory
                .selectFrom(together)
                .innerJoin(together.member, member)
                .innerJoin(together.category, category)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        categoryNameEq(categoryName)
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final List<TogetherResponse> togetherResponses = content.stream().map(TogetherResponse::from).toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(together)
                .innerJoin(together.member, member)
                .innerJoin(member.apartment, apartment)
                .innerJoin(together.category, category)
                .where(
                        apartmentCondition(apartId),
                        categoryNameEq(categoryName)
                );

        return PageableExecutionUtils.getPage(togetherResponses, pageable, countQuery::fetchOne);
    }

    @Override
    public Optional<Together> findTogetherForApartId(final String apartId, final Long togetherId) {
        final Together result = jpaQueryFactory
                .selectFrom(together)
                .innerJoin(together.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        together.id.eq(togetherId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SingleTogetherResponseProjection> findTogetherForApartId(final Long memberId,
                                                                             final String apartId,
                                                                             final Long togetherId) {
        jpaQueryFactory.update(together)
                .set(together.saw, together.saw.add(1))
                .where(together.id.eq(togetherId))
                .execute();

        entityManager.clear();

        final SingleTogetherResponseProjection singleTogetherResponseProjection = jpaQueryFactory
                .select(new QSingleTogetherResponseProjection(
                        together.id.as("id"),
                        category.name.as("category"),
                        together.title.as("title"),
                        together.description.as("description"),
                        together.thumbnail.as("thumbnail"),
                        together.saw.as("saw"),
                        together.liked.as("liked"),
                        together.createdBy.as("createdBy"),
                        Expressions.as(
                                JPAExpressions.select(boardLiked)
                                        .from(boardLiked)
                                        .where(
                                                boardLiked.member.id.eq(memberId),
                                                boardLiked.board.id.eq(togetherId)
                                        )
                                        .exists(),
                                "memberLiked"
                        ),
                        Expressions.as(
                                JPAExpressions.select(together)
                                        .from(together)
                                        .where(
                                                together.id.eq(togetherId),
                                                together.member.id.eq(memberId)
                                        )
                                        .exists(),
                                "memberCreated"
                        ),
                        member.profileImageUrl.as("profileImage"),
                        together.createdAt.as("createdAt"),
                        together.recruitFrom.as("recruitFrom"),
                        together.recruitTo.as("recruitTo"),
                        together.meetTime.as("meetTime"),
                        together.location.as("location"),
                        together.target.as("target"),
                        together.contributeStatus.as("contributeStatus"),
                        together.recruitStatus.as("recruitStatus"),
                        together.thumbnail.as("content")
                ))
                .from(together)
                .innerJoin(together.member, member)
                .innerJoin(together.category, category)
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        together.id.eq(togetherId))
                .fetchOne();

        return Optional.ofNullable(singleTogetherResponseProjection);
    }

    private BooleanExpression categoryNameEq(final String categoryName) {
        return StringUtils.hasText(categoryName) ? category.name.eq(categoryName) : null;
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        final NumberExpression<Integer> recruitStatusPath = new CaseBuilder()
                .when(together.recruitStatus.eq(RecruitStatus.STILL)).then(1)
                .when(together.recruitStatus.eq(RecruitStatus.NOT_YET)).then(2)
                .otherwise(3);

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "saw":
                        OrderSpecifier<?> saw = QueryDslUtil.getSortedColumn(direction, together, "saw");
                        ORDERS.add(saw);
                        break;
                    case "liked":
                        OrderSpecifier<?> liked = QueryDslUtil.getSortedColumn(direction, together, "liked");
                        ORDERS.add(liked);
                        break;
//                    case "recruitStatus":
//                        OrderSpecifier<?> recruitStatus = QueryDslUtil.getSortedColumn(direction, together, "recruitStatus");
//                        OrderSpecifier<?> recruitStatus = QueryDslUtil.getSortedColumn(direction, together, "recruitStatus");
//                        ORDERS.add(recruitStatus);
//                        break;
                    default:
                        OrderSpecifier<?> createdAt = QueryDslUtil.getSortedColumn(direction, together, "createdAt");
                        ORDERS.add(createdAt);
                        break;
                }
            }
        }
        return ORDERS;
    }

}