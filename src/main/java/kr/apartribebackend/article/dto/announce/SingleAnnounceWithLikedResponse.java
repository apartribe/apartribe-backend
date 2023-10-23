package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.likes.dto.BoardLikedRes;

import java.time.LocalDateTime;

public record SingleAnnounceWithLikedResponse(
        Long id,
        String createdBy,
        String profileImage,
        LocalDateTime createdAt,
        String level,
        String title,
        String content,
        int liked,
        boolean memberLiked,
        int saw
) {
    public static SingleAnnounceWithLikedResponse from(SingleAnnounceResponse singleAnnounceResponse, BoardLikedRes boardLikedRes) {
        return new SingleAnnounceWithLikedResponse(
                singleAnnounceResponse.id(),
                singleAnnounceResponse.createdBy(),
                singleAnnounceResponse.profileImage(),
                singleAnnounceResponse.createdAt(),
                singleAnnounceResponse.level(),
                singleAnnounceResponse.title(),
                singleAnnounceResponse.content(),
                singleAnnounceResponse.liked(),
                boardLikedRes.liked(),
                singleAnnounceResponse.saw()
        );
    }
}
