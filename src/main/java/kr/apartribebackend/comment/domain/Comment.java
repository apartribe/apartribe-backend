package kr.apartribebackend.comment.domain;

import jakarta.persistence.*;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.global.domain.BaseEntity;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "COMMENT")
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "LIKES")
    private int liked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ARTICLE_ID")
//    private Article article;

    @Builder
    private Comment(Long id,
                    String content,
                    int liked,
                    Board board,
                    Member member) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.board = board;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public void registBoard(Board board) {
        if (this.board != null)
            this.board.getComments().remove(this);
        this.board = board;
        board.getComments().add(this);
    }

}
