package kr.apartribebackend.article.repository;

import kr.apartribebackend.article.domain.Board;

import java.util.Optional;

public interface CustomBoardRepository {

    Optional<Board> findBoardForApartId(String apartId, Long boardId);

}
