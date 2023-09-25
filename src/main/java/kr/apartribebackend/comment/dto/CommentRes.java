package kr.apartribebackend.comment.dto;

import kr.apartribebackend.article.dto.SingleCommentResponse;
import kr.apartribebackend.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CommentRes(
        Long id,
        String content,
        Integer liked,
        LocalDateTime createdAt,
        String createdBy,
        List<CommentRes> children
) {

    public static CommentRes from(Comment comment) {
        return new CommentRes(
                comment.getId(),
                comment.getContent(),
                comment.getLiked(),
                comment.getCreatedAt(),
                comment.getCreatedBy(),
                comment.getChildren()
                        .stream()
                        .map(CommentRes::from)
                        .collect(Collectors.toList())
        );
    }

}
