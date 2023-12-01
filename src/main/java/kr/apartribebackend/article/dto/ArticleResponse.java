package kr.apartribebackend.article.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private Long id;
    private int liked;
    private int saw;
    private String title;
    private String content;
    private String thumbnail;
    private long commentCounts;
    private LocalDateTime createdAt;
    private String createdBy;
    private String profileImage;
    private boolean onlyApartUser;

    @QueryProjection
    public ArticleResponse(Long id,
                           int liked,
                           int saw,
                           String title,
                           String content,
                           String thumbnail,
                           long commentCounts,
                           LocalDateTime createdAt,
                           String createdBy,
                           String profileImage,
                           boolean onlyApartUser) {
        this.id = id;
        this.liked = liked;
        this.saw = saw;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.commentCounts = commentCounts;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.profileImage = profileImage;
        this.onlyApartUser = onlyApartUser;
    }

}
