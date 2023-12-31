package kr.apartribebackend.article.dto;

import kr.apartribebackend.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SingleCommentResponse(
        Long id,
        String content,
        Integer liked,
        LocalDateTime createdAt,
        String createdBy,
        List<SingleCommentResponse> children
) {
    public static SingleCommentResponse from(Comment comment) {
        return new SingleCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getLiked(),
                comment.getCreatedAt(),
                comment.getCreatedBy(),
                comment.getChildren()
                        .stream()
                        .map(SingleCommentResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
