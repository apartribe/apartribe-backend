package kr.apartribebackend.article.controller;

import kr.apartribebackend.article.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"/api/board/{id}/like", "/api/board/like"})
    public void updateLikeByBoardId(@PathVariable final Optional<Long> id) {
        final Long boardId = id.orElse(0L);
        boardService.updateLikeByBoardId(boardId);
    }

}
