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


//package kr.apartribebackend.article.dto;
//
//
//
//import kr.apartribebackend.article.domain.Article;
//import kr.apartribebackend.member.domain.Member;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public record ArticleResponse(
//        Long id,
//        int liked,
//        int saw,
//        String title,
//        String content,
//        String thumbnail,
//        long commentCounts,
//        LocalDateTime createdAt,
//        String createdBy,
//        String profileImage
//) {
//
//    public static ArticleResponse from(Article article, Member member) {
//        List<SingleCommentResponse> commentResponses = article.getComments()
//                .stream()
//                .map(SingleCommentResponse::from)
//                .toList();
//        return new ArticleResponse(
//                article.getId(),
//                article.getLiked(),
//                article.getSaw(),
//                article.getTitle(),
//                article.getContent(),
//                article.getThumbnail(),
//                commentResponses.size(),
//                article.getCreatedAt(),
//                article.getCreatedBy(),
//                member.getProfileImageUrl()
//        );
//    }
//}
