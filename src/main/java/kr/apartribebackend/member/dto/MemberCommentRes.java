package kr.apartribebackend.member.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberCommentRes {

    private Long id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;

    public MemberCommentRes() {}

    public MemberCommentRes(Long id,
                            String content,
                            String createdBy,
                            LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

}
