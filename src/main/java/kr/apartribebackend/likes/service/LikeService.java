package kr.apartribebackend.likes.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.likes.dto.BoardLikedRes;
import kr.apartribebackend.likes.domain.BoardLiked;
import kr.apartribebackend.likes.repository.BoardLikedRepository;
import kr.apartribebackend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    
    private final BoardLikedRepository boardLikesRepository;

    @Transactional(readOnly = true)
    public Optional<BoardLiked> findBoardLikedByMember(final Long memberId, final Long boardId) {
        return boardLikesRepository.findBoardLikedByMember(memberId, boardId);
    }

    public BoardLikedRes increaseLikesToBoard(final Member member, final Board board) {
        final BoardLiked boardLikes = BoardLiked.builder().board(board).member(member).build();
        boardLikesRepository.save(boardLikes);
        board.reflectArticleLike();
        return new BoardLikedRes(true);
    }

    public BoardLikedRes decreaseLikesToBoard(final BoardLiked boardLiked, final Board board) {
        board.decreaseArticleLike();
        boardLikesRepository.delete(boardLiked);
        return new BoardLikedRes(false);
    }

    @Transactional(readOnly = true)
    public BoardLikedRes isMemberLikedToBoard(final Long memberId, final Long boardId) {
        final Integer memberLikedToBoard = boardLikesRepository.isMemberLikedToBoard(memberId, boardId);
        if (memberLikedToBoard != null) {
            return new BoardLikedRes(true);
        }
        return new BoardLikedRes(false);
    }

}
