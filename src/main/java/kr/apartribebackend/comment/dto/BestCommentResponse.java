package kr.apartribebackend.comment.dto;

import lombok.Getter;

@Getter
public class BestCommentResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private long commentCount;
}
