package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDateTime;

public record SingleArticleResponse(
        Long id,
        String createdBy,
        String profileImage,
        String thumbnail,
        LocalDateTime createdAt,
        String category,
        String title,
        String content,
        int liked,
        int saw,
        boolean onlyApartUser
) {
    public static SingleArticleResponse from(Article article, Member member) {
        article.reflectArticleSaw();
        return new SingleArticleResponse(
                article.getId(),
                article.getCreatedBy(),
                member.getProfileImageUrl(),
                article.getThumbnail(),
                article.getCreatedAt(),
                article.getCategory().getName(),
                article.getTitle(),
                article.getContent(),
                article.getLiked(),
                article.getSaw(),
                article.isOnlyApartUser()
        );
    }
}
