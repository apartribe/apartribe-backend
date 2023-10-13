package kr.apartribebackend.article.repository.together;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.together.SingleTogetherResponse;
import kr.apartribebackend.article.dto.together.TogetherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static kr.apartribebackend.article.domain.QTogether.*;
import static kr.apartribebackend.category.domain.QCategory.category;
import static kr.apartribebackend.comment.domain.QComment.comment;

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
    public List<SingleTogetherResponse> findJoinedTogetherById(final Long togetherId) {
        final List<Together> results = jpaQueryFactory
                .selectFrom(together)
                .leftJoin(together.comments, comment).fetchJoin()
                .where(together.id.eq(togetherId))
                .fetch();

        final List<SingleTogetherResponse> collect = results.stream()
                .map(SingleTogetherResponse::from).toList();
        return collect;
    }

    private BooleanExpression categoryNameEq(final String categoryName) {
        return StringUtils.hasText(categoryName) ? category.name.eq(categoryName) : null;
    }

}