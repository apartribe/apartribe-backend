package kr.apartribebackend.member.dto;

import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.dto.SingleCommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter @AllArgsConstructor @ToString
public class MemberBoardResponse {

    private Long id;
    private String boardType;
    private int liked;
    private int saw;
    private String title;
    private String content;
    private String thumbnail;
    private long commentCounts;
    private LocalDateTime createdAt;
    private String createdBy;

    public static MemberBoardResponse from(Board board) {
        List<SingleCommentResponse> commentResponses = board.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();

        return new MemberBoardResponse(
                board.getId(),
                board.getBoardType(),
                board.getLiked(),
                board.getSaw(),
                board.getTitle(),
                board.getContent(),
                board.getThumbnail(),
                commentResponses.size(),
                board.getCreatedAt(),
                board.getCreatedBy()
        );
    }
}
