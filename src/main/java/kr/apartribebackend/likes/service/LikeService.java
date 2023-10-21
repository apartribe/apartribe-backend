package kr.apartribebackend.likes.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.likes.domain.BoardLiked;
import kr.apartribebackend.likes.repository.BoardLikedRepository;
import kr.apartribebackend.likes.repository.CommentLikedRepository;
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

    public void increaseLikesToBoard(final Member member, final Board board) {
        final BoardLiked boardLikes = BoardLiked.builder().board(board).member(member).build();
        boardLikesRepository.save(boardLikes);
        board.reflectArticleLike();
    }

    public void decreaseLikesToBoard(final BoardLiked boardLiked, final Board board) {
        board.decreaseArticleLike();
        boardLikesRepository.delete(boardLiked);
    }

}
