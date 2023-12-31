package kr.apartribebackend.member.repository;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.domain.QApartment;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.global.utils.QueryDslUtil;
import kr.apartribebackend.member.domain.Badge;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.domain.QMember;
import kr.apartribebackend.member.dto.MemberBoardResponse;
import kr.apartribebackend.member.dto.MemberCommentRes;
import kr.apartribebackend.member.dto.QSingleMemberResponse;
import kr.apartribebackend.member.dto.SingleMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.article.domain.QBoard.board;
import static kr.apartribebackend.category.domain.QCategory.*;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.member.domain.QMember.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
@Repository
public class MemberConfigRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<SingleMemberResponse> findMemberWithApartInfoByMemberIdAndMemberType(final Long memberId,
                                                                                         final MemberType memberType) {
        final SingleMemberResponse singleMemberResponse = jpaQueryFactory
                .select(
                        new QSingleMemberResponse(
                                member.email,
                                member.name,
                                member.nickname,
                                member.profileImageUrl,
                                member.apartCode,
                                member.apartName,
                                member.authStatus,
                                member.position,
                                member.userType
                        )
                )
                .from(member)
                .leftJoin(member.apartment, apartment)
                .where(member.id.eq(memberId), member.memberType.eq(memberType))
                .fetchOne();
        return Optional.ofNullable(singleMemberResponse);
    }

    // TODO 리팩토링을 해야한다. (select 절이 너무나도 많다.)
    // 내가 쓴 댓글 --> 사실 email 이 uniq 해서 아파트 정보와 join 할 필요는 없지만, 연습삼아 해본것이다.
    public Page<MemberCommentRes> findCommentsForMember(final Member member,
                                                        final Apartment apartment,
                                                        final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Comment> memberCommentRes = jpaQueryFactory
                .select(comment)
                .from(comment)
                .innerJoin(comment.member, QMember.member).fetchJoin()
                .innerJoin(comment.board, board).fetchJoin()
                .innerJoin(QMember.member.apartment, QApartment.apartment).fetchJoin()
                .where(
                        QApartment.apartment.code.eq(apartment.getCode()),
                        QApartment.apartment.name.eq(apartment.getName()),
                        QMember.member.id.eq(member.getId())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Board 에는 Category 연관이 없기때문에, together 로 형변환 후, getCaetgory.name() 을 하면 n + 1 이 터진다. 따라서 N + 1 이 터지기전에 미리 아파트에 맞는 카테고리를 fetchJoin 해놓는 것이 효율적이다.
        jpaQueryFactory
                .selectFrom(category)
                .innerJoin(category.apartment, QApartment.apartment).fetchJoin()
                .where(QApartment.apartment.code.eq(apartment.getCode()))
                .fetch();

        List<MemberCommentRes> memberCommentResList = memberCommentRes.stream().map(MemberCommentRes::from).toList();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(Wildcard.count)
                .from(comment)
                .innerJoin(comment.member, QMember.member)
                .innerJoin(QMember.member.apartment, QApartment.apartment)
                .where(comment.member.id.eq(member.getId()));

         return PageableExecutionUtils.getPage(memberCommentResList, pageable, countQuery::fetchOne);
    }

    // TODO 리팩토링을 해야한다. (select 절이 너무나도 많다.)
    public Page<MemberBoardResponse> findArticlesForMember(final Member member,
                                                           final Apartment apartment,
                                                           final Pageable pageable) {
        final List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        // Board 에는 Category 연관이 없기때문에, together 로 형변환 후, getCaetgory.name() 을 하면 n + 1 이 터진다. 따라서 N + 1 이 터지기전에 미리 아파트에 맞는 카테고리를 fetchJoin 해놓는 것이 효율적이다.
        final List<Category> categories = jpaQueryFactory
                .selectFrom(category)
                .innerJoin(category.apartment, QApartment.apartment).fetchJoin()
                .where(QApartment.apartment.code.eq(apartment.getCode()))
                .fetch();

        final List<Board> contents = jpaQueryFactory
                .select(board)
                .from(board)
                .innerJoin(board.member, QMember.member).fetchJoin()
                .innerJoin(QMember.member.apartment, QApartment.apartment).fetchJoin()
                .where(
                        QApartment.apartment.code.eq(apartment.getCode()),
                        QApartment.apartment.name.eq(apartment.getName()),
                        QMember.member.id.eq(member.getId())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final List<MemberBoardResponse> results = contents.stream().map(MemberBoardResponse::from).collect(Collectors.toList());

        final JPAQuery<Long> countQuery = jpaQueryFactory
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

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();
        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "createdAt":
                        OrderSpecifier<?> createdAt = QueryDslUtil.getSortedColumn(direction, board, "createdAt");
                        ORDERS.add(createdAt);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }
}
