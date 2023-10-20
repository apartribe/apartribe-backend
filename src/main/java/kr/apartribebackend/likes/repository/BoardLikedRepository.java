package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.BoardLiked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikedRepository
        extends JpaRepository<BoardLiked, Long>, CustomBoardLikedRepository {


}
