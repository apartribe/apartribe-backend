package kr.apartribebackend.comment.domain;

import jakarta.persistence.*;
import kr.apartribebackend.article.domain.Board;
import kr.apartribebackend.global.domain.BaseEntity;
import kr.apartribebackend.likes.domain.CommentLiked;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;

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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("createdAt desc")
    private final Set<Comment> children = new HashSet<>();

    @OneToMany(mappedBy = "comment")
    private final Set<CommentLiked> commentLikedList = new HashSet<>();

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

    public void registParent(Comment parent) {
        this.parent = parent;
    }

    public Comment updateComment(String content) {
        this.content = content;
        return this;
    }

    public void reflectCommentLike() {
        this.liked += 1;
    }

    public void decreaseCommentLike() {
        this.liked -= 1;
    }
}
