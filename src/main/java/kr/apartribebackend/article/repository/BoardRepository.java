package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {


}
