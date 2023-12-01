package kr.apartribebackend.article.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class JoinedArticleResponse {
    private long articleId;
    private String title;
    private String content;
    private int saw;
    private int likes;
    private String nickname;
    private long commentCounts;
    private List<JoinedCommentResponse> comments;


    @QueryProjection
    public JoinedArticleResponse(long articleId,
                                 String title,
                                 String content,
                                 int saw,
                                 int likes,
                                 String nickname,
                                 long commentCounts,
                                 List<JoinedCommentResponse> comments) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.saw = saw;
        this.likes = likes;
        this.nickname = nickname;
        this.commentCounts = commentCounts;
        this.comments = comments;
    }

}
