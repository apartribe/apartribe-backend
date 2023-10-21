package kr.apartribebackend.article.repository.together;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.SingleTogetherResponse;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import kr.apartribebackend.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.apartment;
import static kr.apartribebackend.article.domain.QTogether.*;
import static kr.apartribebackend.category.domain.QCategory.category;
import static kr.apartribebackend.comment.domain.QComment.comment;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomTogetherRepositoryImpl implements CustomTogetherRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TogetherResponse> findMultipleTogethersByCategory(final String categoryName, final Pageable pageable) {
        List<Together> content = jpaQueryFactory
                .selectFrom(together)
                .innerJoin(together.category, category)
                .where(categoryNameEq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final List<TogetherResponse> togetherResponses = content.stream().map(TogetherResponse::from).toList();

        final JPAQuery<Long> countQuery = jpaQueryFactory
                .select(together.count())
                .from(together)
                .innerJoin(together.category, category)
                .where(categoryNameEq(categoryName));

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

    private BooleanExpression categoryNameEq(final String categoryName) {
        return StringUtils.hasText(categoryName) ? category.name.eq(categoryName) : null;
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }

}