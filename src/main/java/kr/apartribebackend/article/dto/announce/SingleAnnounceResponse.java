package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.dto.SingleCommentResponse;

import java.util.List;
import java.util.stream.Collectors;

public record SingleAnnounceResponse(
        Long id,
        String createdBy,
        String level,
        String title,
        String content,
        int liked,
        int saw,
        long commentCounts
//        List<SingleCommentResponse> comments
) {
    public static SingleAnnounceResponse from(Announce announce) {
        announce.reflectArticleSaw();
        List<SingleCommentResponse> commentResponses = announce.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();
        return new SingleAnnounceResponse(
                announce.getId(),
                announce.getCreatedBy(),
                announce.getLevel().getName(),
                announce.getTitle(),
                announce.getContent(),
                announce.getLiked(),
                announce.getSaw(),
                commentResponses.size()
//                commentResponses
        );
    }
}
