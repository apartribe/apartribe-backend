package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.BoardLiked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardLikedRepository
        extends JpaRepository<BoardLiked, Long>, CustomBoardLikedRepository {

    @Query(value = "select bl from BoardLiked as bl where bl.board.id = :boardId")
    List<BoardLiked> findBoardLikedsByBoardId(@Param("boardId") Long boardId);

}
