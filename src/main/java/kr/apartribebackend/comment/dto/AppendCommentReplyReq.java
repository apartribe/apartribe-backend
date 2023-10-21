package kr.apartribebackend.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AppendCommentReplyReq(
        @NotNull(message = "부모 댓글은 공백일 수 없습니다.") Long parentId,
        @NotEmpty(message = "댓글 내용은 공백일 수 없습니다.") String content
) {

    public CommentDto toDto() {
        return CommentDto.builder()
                .content(content)
                .build();
    }
}
