package kr.apartribebackend.article.dto;


import kr.apartribebackend.article.domain.Article;

public record ArticleInCommunityRes(
        Long id,
        String title
) {
    public static ArticleInCommunityRes from(Article article) {
        return new ArticleInCommunityRes(
                article.getId(),
                article.getTitle()
        );
    }
}
