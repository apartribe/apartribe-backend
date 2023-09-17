package kr.apartribebackend.article.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommentSingleResultResponse {
    private Long id;
    private String content;
    private Integer liked;
    private String createdBy;

    @QueryProjection
    public CommentSingleResultResponse(Long id,
                                       String content,
                                       Integer liked,
                                       String createdBy) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.createdBy = createdBy;
    }

}
