package kr.apartribebackend.likes.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.apartribebackend.likes.domain.BoardLiked;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static kr.apartribebackend.article.domain.QBoard.*;
import static kr.apartribebackend.likes.domain.QBoardLiked.*;
import static kr.apartribebackend.member.domain.QMember.*;

@RequiredArgsConstructor
public class CustomBoardLikedRepositoryImpl implements CustomBoardLikedRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BoardLiked> findBoardLikedByMember(final Long memberId, final Long boardId) {
        BoardLiked result = jpaQueryFactory
                .select(boardLiked)
                .from(boardLiked)
                .innerJoin(boardLiked.board, board)
                .innerJoin(boardLiked.member, member)
                .where(
                        member.id.eq(memberId),
                        board.id.eq(boardId)
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Integer isMemberLikedToBoard(Long memberId, Long boardId) {
        return jpaQueryFactory
                .selectOne()
                .from(boardLiked)
                .innerJoin(boardLiked.member, member)
                .innerJoin(boardLiked.board, board)
                .where(
                        member.id.eq(memberId),
                        board.id.eq(boardId)
                )
                .fetchFirst();
    }

}
