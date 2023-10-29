package kr.apartribebackend.article.dto;

import kr.apartribebackend.article.domain.Article;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        int saw
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
                article.getSaw()
        );
    }
}



//package kr.apartribebackend.article.dto;
//
//import kr.apartribebackend.article.domain.Article;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public record SingleArticleResponse(
//        Long id,
//        String createdBy,
//        LocalDateTime createdAt,
//        String category,
//        String title,
//        String content,
//        int liked,
//        int saw,
//        long commentCounts
////        List<SingleCommentResponse> comments
//) {
//    public static SingleArticleResponse from(Article article) {
//        article.reflectArticleSaw();
//        List<SingleCommentResponse> commentResponses = article.getComments()
//                .stream()
//                .map(SingleCommentResponse::from)
//                .toList();
//        return new SingleArticleResponse(
//                article.getId(),
//                article.getCreatedBy(),
//                article.getCreatedAt(),
//                article.getCategory().getName(),
//                article.getTitle(),
//                article.getContent(),
//                article.getLiked(),
//                article.getSaw(),
//                commentResponses.size()
////                commentResponses
//        );
//    }
//
//}
