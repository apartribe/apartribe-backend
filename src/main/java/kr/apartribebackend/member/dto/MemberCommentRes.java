package kr.apartribebackend.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static kr.apartribebackend.article.domain.QArticle.article;

@Getter @Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class MemberCommentRes {

    private Long id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private String boardTitle;
    private String boardType;
    private String category;
    private String level;
    private Long boardId;
    private String apartCode;

    public static MemberCommentRes from(Comment comment) {
        final Board board = comment.getBoard();

        final MemberCommentResBuilder memberCommentResBuilder = MemberCommentRes.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdBy(comment.getCreatedBy())
                .createdAt(comment.getCreatedAt())
                .boardTitle(board.getTitle())
                .boardType(board.getBoardType())
                .boardId(board.getId())
                .apartCode(comment.getMember().getApartment().getCode());

        if (board instanceof Article article) {
            return memberCommentResBuilder.category(article.getCategory().getName()).build();
        }
        if (board instanceof Announce announce) {
            return memberCommentResBuilder.level(announce.getLevel().getName()).build();
        }
        if (board instanceof Together together) {
            return memberCommentResBuilder.category(together.getCategory().getName()).build();
        }
        return null;
    }

}
