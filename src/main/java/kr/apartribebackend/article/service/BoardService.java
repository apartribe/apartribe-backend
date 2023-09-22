package kr.apartribebackend.article.service;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.exception.CannotReflectLikeToArticleException;
import kr.apartribebackend.article.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void updateLikeByBoardId(final Long articleId) {
        boardRepository.findById(articleId)
                .ifPresentOrElse(Board::reflectArticleLike,
                        () -> { throw new CannotReflectLikeToArticleException(); });
    }

}
