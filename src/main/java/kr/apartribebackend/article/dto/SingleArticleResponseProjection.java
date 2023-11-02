package kr.apartribebackend.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SingleArticleResponseProjection {
    private Long id;
    private String createdBy;
    private boolean memberCreated;
    boolean memberLiked;
    private String profileImage;
    private String thumbnail;
    private LocalDateTime createdAt;
    private String category;
    private String title;
    private String content;
    private int liked;
    private int saw;
}
