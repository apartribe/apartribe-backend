package kr.apartribebackend.article.dto.together;

import kr.apartribebackend.likes.dto.BoardLikedRes;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SingleTogetherWithLikedResponse(
        Long id,
        String category,
        String title,
        String thumbnail,
        int saw,
        int liked,
        boolean memberLiked,
        String createdBy,
        String profileImage,
        LocalDateTime createdAt,
        LocalDate recruitFrom,
        LocalDate recruitTo,
        String meetTime,
        String location,
        String target,
        boolean contributeStatus,
        String recruitStatus,
        String content
) {
    public static SingleTogetherWithLikedResponse from(SingleTogetherResponse singleTogetherResponse, BoardLikedRes boardLikedRes) {
        return new SingleTogetherWithLikedResponse(
                singleTogetherResponse.id(),
                singleTogetherResponse.category(),
                singleTogetherResponse.title(),
                singleTogetherResponse.thumbnail(),
                singleTogetherResponse.saw(),
                singleTogetherResponse.liked(),
                boardLikedRes.liked(),
                singleTogetherResponse.createdBy(),
                singleTogetherResponse.profileImage(),
                singleTogetherResponse.createdAt(),
                singleTogetherResponse.recruitFrom(),
                singleTogetherResponse.recruitTo(),
                singleTogetherResponse.meetTime(),
                singleTogetherResponse.location(),
                singleTogetherResponse.target(),
                singleTogetherResponse.contributeStatus(),
                singleTogetherResponse.recruitStatus(),
                singleTogetherResponse.content()
        );
    }

}
