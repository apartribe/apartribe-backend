package kr.apartribebackend.article.dto;

import kr.apartribebackend.comment.domain.Comment;

import java.time.LocalDateTime;

public record SingleCommentResponse(
        Long id,
        String content,
        Integer liked,
        LocalDateTime createdAt,
        String createdBy
) {
    public static SingleCommentResponse from(Comment comment) {
        return new SingleCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getLiked(),
                comment.getCreatedAt(),
                comment.getCreatedBy()
        );
    }
}
