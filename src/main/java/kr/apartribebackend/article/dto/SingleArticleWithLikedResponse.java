package kr.apartribebackend.article.dto;

import kr.apartribebackend.likes.dto.BoardLikedRes;

import java.time.LocalDateTime;

public record SingleArticleWithLikedResponse(
        Long id,
        String createdBy,
        String profileImage,
        String thumbnail,
        LocalDateTime createdAt,
        String category,
        String title,
        String content,
        int liked,
        boolean memberLiked,
        int saw
) {
    public static SingleArticleWithLikedResponse from(SingleArticleResponse singleArticleResponse, BoardLikedRes boardLikedRes) {
        return new SingleArticleWithLikedResponse(
                singleArticleResponse.id(),
                singleArticleResponse.createdBy(),
                singleArticleResponse.profileImage(),
                singleArticleResponse.thumbnail(),
                singleArticleResponse.createdAt(),
                singleArticleResponse.category(),
                singleArticleResponse.title(),
                singleArticleResponse.content(),
                singleArticleResponse.liked(),
                boardLikedRes.liked(),
                singleArticleResponse.saw()
        );
    }

}
