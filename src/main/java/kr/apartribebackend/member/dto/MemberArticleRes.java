package kr.apartribebackend.member.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberArticleRes {

    private Long id;
    private String category;
    private String title;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;

    public MemberArticleRes() {}

    public MemberArticleRes(Long id,
                            String category,
                            String title,
                            String content,
                            String createdBy,
                            LocalDateTime createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

}
