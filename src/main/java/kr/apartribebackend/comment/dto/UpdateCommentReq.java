package kr.apartribebackend.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentReq(
        @NotNull(message = "댓글 ID 는 공백일 수 없습니다.") Long commentId,
        @NotEmpty(message = "댓글 내용은 공백일 수 없습니다.") String content
) {

    public CommentDto toDto() {
        return CommentDto.builder()
                .id(commentId)
                .content(content)
                .build();
    }
}
