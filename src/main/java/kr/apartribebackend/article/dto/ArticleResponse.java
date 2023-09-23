package kr.apartribebackend.article.dto;



import kr.apartribebackend.article.domain.Article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
//        String category,
        int liked,
        int saw,
        String title,
        String content,
        LocalDateTime createdAt,
        String createdBy
) {

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
//                article.getCategory().getName(),
                article.getLiked(),
                article.getSaw(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getCreatedBy()
        );
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        return new ArticleResponse(
                articleDto.getId(),
//                articleDto.getCategory().getName(),
                articleDto.getLiked(),
                articleDto.getSaw(),
                articleDto.getTitle(),
                articleDto.getContent(),
                articleDto.getCreatedAt(),
                articleDto.getCreatedBy()
        );
    }
}


//package kr.apartribebackend.article.dto;
//
//
//import java.time.LocalDateTime;
//
//public record ArticleResponse(
//        Long id,
//        String category,
//        int liked,
//        int saw,
//        String title,
//        String content,
//        LocalDateTime createdAt,
//        String createdBy
//) {
//
//    public static ArticleResponse from(ArticleDto articleDto) {
//        return new ArticleResponse(
//                articleDto.getId(),
//                articleDto.getCategory().getName(),
//                articleDto.getLiked(),
//                articleDto.getSaw(),
//                articleDto.getTitle(),
//                articleDto.getContent(),
//                articleDto.getCreatedAt(),
//                articleDto.getCreatedBy()
//        );
//    }
//}
