package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;

import java.util.List;
import java.util.stream.Collectors;

public record SingleArticleResponse(
        Long id,
        String createdBy,
        String category,
        String title,
        String content,
        int liked,
        int saw,
        long commentCounts
//        List<SingleCommentResponse> comments
) {
    public static SingleArticleResponse from(Article article) {
        article.reflectArticleSaw();
        List<SingleCommentResponse> commentResponses = article.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();
        return new SingleArticleResponse(
                article.getId(),
                article.getCreatedBy(),
                article.getCategory().getName(),
                article.getTitle(),
                article.getContent(),
                article.getLiked(),
                article.getSaw(),
                commentResponses.size()
//                commentResponses
        );
    }

}
