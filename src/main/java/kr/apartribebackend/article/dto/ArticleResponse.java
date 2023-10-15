package kr.apartribebackend.article.dto;



import kr.apartribebackend.article.domain.Article;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
        Long id,
//        String category,
        int liked,
        int saw,
        String title,
        String content,
        long commentCounts,
        LocalDateTime createdAt,
        String createdBy
) {

    public static ArticleResponse from(Article article) {
        List<SingleCommentResponse> commentResponses = article.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();
        return new ArticleResponse(
                article.getId(),
//                article.getCategory().getName(),
                article.getLiked(),
                article.getSaw(),
                article.getTitle(),
                article.getContent(),
                commentResponses.size(),
                article.getCreatedAt(),
                article.getCreatedBy()
        );
    }
}

//    public static ArticleResponse from(ArticleDto articleDto) {
//        return new ArticleResponse(
//                articleDto.getId(),
////                articleDto.getCategory().getName(),
//                articleDto.getLiked(),
//                articleDto.getSaw(),
//                articleDto.getTitle(),
//                articleDto.getContent(),
//                articleDto.getCreatedAt(),
//                articleDto.getCreatedBy()
//        );
//    }
