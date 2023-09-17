package kr.apartribebackend.article.dto;

import kr.apartribebackend.comment.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SingleCommentResponse {
    private Long id;
    private String content;
    private Integer liked;
    private LocalDateTime createdAt;
    private String createdBy;

    public SingleCommentResponse(Comment comment) {
        id = comment.getId();
        content = comment.getContent();
        liked = comment.getLiked();
        createdAt = comment.getCreatedAt();
        createdBy = comment.getCreatedBy();
    }
}
