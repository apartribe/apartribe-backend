package kr.apartribebackend.article.domain;

import jakarta.persistence.*;
import kr.apartribebackend.attachment.domain.Attachment;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.global.domain.BaseEntity;
import kr.apartribebackend.member.domain.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @SuperBuilder
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "BOARD_TYPE")
public abstract class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "CONTENT", columnDefinition="TEXT", nullable = false)
    private String content;

    @Column(name = "LIKES")
    private int liked;

    @Column(name = "SAW")
    private int saw;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "board")
    @OrderBy("createdAt desc")
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)                                            // TODO 여기가 추가됨.
    private final List<Attachment> attachments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return id != null && Objects.equals(id, board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public void reflectArticleLike() {
        this.liked += 1;
    }

    public void reflectArticleSaw() {
        this.saw += 1;
    }
}
