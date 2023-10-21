package kr.apartribebackend.comment.dto;


import java.time.LocalDateTime;

public record SingleCommentResponse(
        Long id,
        String content,
        String createdBy,
        String profileImage,
        LocalDateTime createdAt
) {
    public static SingleCommentResponse from(CommentDto commentDto) {
        return new SingleCommentResponse(
                commentDto.getId(),
                commentDto.getContent(),
                commentDto.getCreatedBy(),
                commentDto.getMemberDto().getProfileImageUrl(),
                commentDto.getCreatedAt()
        );
    }
}
