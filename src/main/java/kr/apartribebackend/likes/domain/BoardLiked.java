package kr.apartribebackend.likes.domain;

import jakarta.persistence.*;
import kr.apartribebackend.article.domain.Board;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder @Getter
@Entity @Table(name = "BOARD_LIKED")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BOARD")
public class BoardLiked extends Liked {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

}
