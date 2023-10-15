package kr.apartribebackend.article.dto.announce;


import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.dto.SingleCommentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record AnnounceResponse(
        Long id,
        String level,
        int liked,
        int saw,
        String title,
        String content,
        String thumbnail,
        long commentCounts,
        LocalDateTime createdAt,
        String createdBy
) {

    public static AnnounceResponse from(Announce announce) {
        List<SingleCommentResponse> commentResponses = announce.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();
        return new AnnounceResponse(
                announce.getId(),
                announce.getLevel().getName(),
                announce.getLiked(),
                announce.getSaw(),
                announce.getTitle(),
                announce.getContent(),
                announce.getThumbnail(),
                commentResponses.size(),
                announce.getCreatedAt(),
                announce.getCreatedBy()
        );
    }

}

//    public static AnnounceResponse from(AnnounceDto announceDto) {
//        return new AnnounceResponse(
//                announceDto.getId(),
//                announceDto.getLevel().getName(),
//                announceDto.getLiked(),
//                announceDto.getSaw(),
//                announceDto.getTitle(),
//                announceDto.getContent(),
//                announceDto.getCreatedAt(),
//                announceDto.getCreatedBy()
//        );
//    }
