package kr.apartribebackend.article.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class JoinedCommentResponse {
    private String writtenBy;
    private String value;
    private int likes;

    @QueryProjection
    public JoinedCommentResponse(String writtenBy, String value, int likes) {
        this.writtenBy = writtenBy;
        this.value = value;
        this.likes = likes;
    }
}
