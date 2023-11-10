package kr.apartribebackend.likes.dto;


import lombok.Getter;

@Getter
public class CommentLikedWithCommentAndMember {
    private Long commentId;
    private Long memberId;
}
