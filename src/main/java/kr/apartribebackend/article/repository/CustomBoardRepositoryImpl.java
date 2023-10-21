package kr.apartribebackend.article.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.article.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static kr.apartribebackend.apart.domain.QApartment.apartment;
import static kr.apartribebackend.article.domain.QBoard.*;
import static kr.apartribebackend.member.domain.QMember.member;

@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Board> findBoardForApartId(final String apartId, Long boardId) {
        final Board result = jpaQueryFactory
                .selectFrom(board)
                .innerJoin(board.member, member).fetchJoin()
                .innerJoin(member.apartment, apartment)
                .where(
                        apartmentCondition(apartId),
                        board.id.eq(boardId)
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    private BooleanExpression apartmentCondition(final String apartId) {
        return StringUtils.hasText(apartId) ? apartment.code.eq(apartId) : null;
    }
}
