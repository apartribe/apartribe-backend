package kr.apartribebackend.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.SingleCommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Builder
@Getter @AllArgsConstructor @ToString
public class MemberBoardResponse {

    private Long id;
    private String boardType;
    private String category;
    private String level;
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

        final MemberBoardResponseBuilder memberBoardResponseBuilder = MemberBoardResponse.builder()
                .id(board.getId())
                .liked(board.getLiked())
                .saw(board.getSaw())
                .title(board.getTitle())
                .content(board.getContent())
                .thumbnail(board.getThumbnail())
                .commentCounts(commentResponses.size())
                .createdAt(board.getCreatedAt())
                .createdBy(board.getCreatedBy())
                .boardType(board.getBoardType());

        MemberBoardResponse memberBoardResponse = null;
        if (board instanceof Article article) {
            memberBoardResponse = memberBoardResponseBuilder.category(article.getCategory().getName()).build();
        } else if (board instanceof Together together) {
            memberBoardResponse = memberBoardResponseBuilder.category(together.getCategory().getName()).build();
        } else if (board instanceof Announce announce) {
            memberBoardResponse = memberBoardResponseBuilder.level(announce.getLevel().getName()).build();
        }
        return memberBoardResponse;
    }
}
