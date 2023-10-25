package kr.apartribebackend.category.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.category.domain.Category;
import kr.apartribebackend.category.dto.CategoryListRes;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.*;
import static kr.apartribebackend.category.domain.QCategory.*;

@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Category> findCategoryByTagAndNameWithApart(final String apartId,
                                                                final String tag,
                                                                final String name) {
        final Category result = jpaQueryFactory
                .selectFrom(category)
                .innerJoin(category.apartment, apartment)
                .where(
                        category.tag.eq(tag),
                        category.name.eq(name),
                        apartment.code.eq(apartId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<CategoryListRes> findCategoriesByTagWithApart(final String apartId, final String tag) {
        return jpaQueryFactory
                .select(Projections.fields(CategoryListRes.class,
                        category.name.as("categoryName")
                ))
                .from(category)
                .innerJoin(category.apartment, apartment)
                .where(
                        category.tag.eq(tag),
                        apartment.code.eq(apartId)
                )
                .fetch();
    }
}
