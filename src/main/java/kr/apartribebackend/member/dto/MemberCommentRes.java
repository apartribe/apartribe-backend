package kr.apartribebackend.member.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberCommentRes {

    private Long id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;
    private String boardType;
    private Long boardId;

    public MemberCommentRes() {}

    public MemberCommentRes(Long id,
                            String content,
                            String createdBy,
                            LocalDateTime createdAt,
                            String boardType,
                            Long boardId) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.boardType = boardType;
        this.boardId = boardId;
    }

}
