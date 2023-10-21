package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDateTime;

public record SingleAnnounceResponse(
        Long id,
        String createdBy,
        String profileImage,
        LocalDateTime createdAt,
        String level,
        String title,
        String content,
        int liked,
        int saw
) {
    public static SingleAnnounceResponse from(Announce announce, Member member) {
        announce.reflectArticleSaw();
        return new SingleAnnounceResponse(
                announce.getId(),
                announce.getCreatedBy(),
                member.getProfileImageUrl(),
                announce.getCreatedAt(),
                announce.getLevel().getName(),
                announce.getTitle(),
                announce.getContent(),
                announce.getLiked(),
                announce.getSaw()
        );
    }
}


//
//
//package kr.apartribebackend.article.dto.announce;
//
//import kr.apartribebackend.article.domain.Announce;
//
//import java.time.LocalDateTime;
//
//public record SingleAnnounceResponse(
//        Long id,
//        String createdBy,
//        LocalDateTime createdAt,
//        String level,
//        String title,
//        String content,
//        int liked,
//        int saw
////        long commentCounts
////        List<SingleCommentResponse> comments
//) {
//    public static SingleAnnounceResponse from(Announce announce) {
//        announce.reflectArticleSaw();
////        List<SingleCommentResponse> commentResponses = announce.getComments()
////                .stream()
////                .map(SingleCommentResponse::from)
////                .toList();
//        return new SingleAnnounceResponse(
//                announce.getId(),
//                announce.getCreatedBy(),
//                announce.getCreatedAt(),
//                announce.getLevel().getName(),
//                announce.getTitle(),
//                announce.getContent(),
//                announce.getLiked(),
//                announce.getSaw()
////                commentResponses.size()
////                commentResponses
//        );
//    }
//}
