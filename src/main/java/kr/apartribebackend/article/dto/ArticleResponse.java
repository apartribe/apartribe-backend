package kr.apartribebackend.article.dto;



import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
        Long id,
//        String category,
        int liked,
        int saw,
        String title,
        String content,
        String thumbnail,
        long commentCounts,
        LocalDateTime createdAt,
        String createdBy,
        String profileImage
) {

    public static ArticleResponse from(Article article, Member member) {
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
                article.getThumbnail(),
                commentResponses.size(),
                article.getCreatedAt(),
                article.getCreatedBy(),
                member.getProfileImageUrl()
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
