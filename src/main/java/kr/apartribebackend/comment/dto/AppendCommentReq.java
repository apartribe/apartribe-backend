package kr.apartribebackend.comment.dto;

import jakarta.validation.constraints.NotEmpty;

public record AppendCommentReq(
        @NotEmpty(message = "댓글 내용은 공백일 수 없습니다.") String content
) {

    public CommentDto toDto() {
        return CommentDto.builder()
                .content(content)
                .build();
    }
}
