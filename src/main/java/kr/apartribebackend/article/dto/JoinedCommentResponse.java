package kr.apartribebackend.article.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.beans.ConstructorProperties;

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
