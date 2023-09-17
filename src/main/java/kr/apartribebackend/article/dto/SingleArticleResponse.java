package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SingleArticleResponse {
    private Long id;
    private String createdBy;
    private String title;
    private String content;
    private int liked;
    private int saw;
    private long commentCounts;
    private List<SingleCommentResponse> comments;

    public SingleArticleResponse(Article article) {

        List<SingleCommentResponse> commentResponses = article.getComments()
                .stream()
                .map(SingleCommentResponse::new)
                .collect(Collectors.toList());

        article.reflectArticleSaw();
        id = article.getId();
        createdBy = article.getCreatedBy();
        title = article.getTitle();
        content = article.getContent();
        liked = article.getLiked();
        saw = article.getSaw();
        commentCounts = commentResponses.size();
        comments = commentResponses;
    }

}
