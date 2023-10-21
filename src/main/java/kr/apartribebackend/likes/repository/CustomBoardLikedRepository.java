package kr.apartribebackend.likes.repository;

import kr.apartribebackend.likes.domain.BoardLiked;

import java.util.Optional;

public interface CustomBoardLikedRepository {

    Optional<BoardLiked> findBoardLikedByMember(Long memberId, Long boardId);

}
