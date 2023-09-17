package kr.apartribebackend.article.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ArticleSingleResultResponse {
    private Long id;
    private String title;
    private String content;
    private int liked;
    private int saw;
    private long commentCounts;
    private List<CommentSingleResultResponse> comments;

    @QueryProjection
    public ArticleSingleResultResponse(Long id,
                                       String title,
                                       String content,
                                       int liked,
                                       int saw,
                                       long commentCounts,
                                       List<CommentSingleResultResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.saw = saw;
        this.commentCounts = commentCounts;
        this.comments = comments.size() == 0 ? new ArrayList<>() : comments;
    }
}
