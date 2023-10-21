package kr.apartribebackend.likes.domain;

import jakarta.persistence.*;
import kr.apartribebackend.comment.domain.Comment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder @Getter
@Entity @Table(name = "COMMENT_LIKED")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("COMMENT")
public class CommentLiked extends Liked {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID")
    private Comment comment;

}
