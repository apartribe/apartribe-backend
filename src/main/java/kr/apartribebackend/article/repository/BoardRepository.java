package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository
        extends JpaRepository<Board, Long>, CustomBoardRepository {

    @Query(value = "select b from Board as b where b.member.id = :memberId")
    List<Board> findBoardsByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Board as b where b.id in :boardIds")
    int deleteBoardsUsingBoardIds(@Param("boardIds") List<Long> boardIds);

}
