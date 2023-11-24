package kr.apartribebackend.article.dto.announce;

import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.member.domain.Position;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnnounceResponse {

    private Long id;
    private String level;
    private int liked;
    private int saw;
    private String title;
    private String content;
    private String thumbnail;
    private long commentCounts;
    private LocalDateTime createdAt;
    private String createdBy;
    private String profileImage;
    private boolean onlyApartUser;
    private String position;

    @QueryProjection
    public AnnounceResponse(Long id,
                            Level level,
                            int liked,
                            int saw,
                            String title,
                            String content,
                            String thumbnail,
                            long commentCounts,
                            LocalDateTime createdAt,
                            String createdBy,
                            String profileImage,
                            boolean onlyApartUser,
                            Position position) {
        this.id = id;
        this.level = level.getName();
        this.liked = liked;
        this.saw = saw;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.commentCounts = commentCounts;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.profileImage = profileImage;
        this.onlyApartUser = onlyApartUser;
        this.position = position.getName();
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


//package kr.apartribebackend.article.dto.announce;
//
//
//import kr.apartribebackend.article.domain.Announce;
//import kr.apartribebackend.article.dto.SingleCommentResponse;
//import kr.apartribebackend.member.domain.Member;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//public record AnnounceResponse(
//        Long id,
//        String level,
//        int liked,
//        int saw,
//        String title,
//        String content,
//        String thumbnail,
//        long commentCounts,
//        LocalDateTime createdAt,
//        String createdBy,
//        String profileImage
//) {
//
//    public static AnnounceResponse from(Announce announce, Member member) {
//        List<SingleCommentResponse> commentResponses = announce.getComments()
//                .stream()
//                .map(SingleCommentResponse::from)
//                .toList();
//        return new AnnounceResponse(
//                announce.getId(),
//                announce.getLevel().getName(),
//                announce.getLiked(),
//                announce.getSaw(),
//                announce.getTitle(),
//                announce.getContent(),
//                announce.getThumbnail(),
//                commentResponses.size(),
//                announce.getCreatedAt(),
//                announce.getCreatedBy(),
//                member.getProfileImageUrl()
//        );
//    }
//
//}
//
////    public static AnnounceResponse from(AnnounceDto announceDto) {
////        return new AnnounceResponse(
////                announceDto.getId(),
////                announceDto.getLevel().getName(),
////                announceDto.getLiked(),
////                announceDto.getSaw(),
////                announceDto.getTitle(),
////                announceDto.getContent(),
////                announceDto.getCreatedAt(),
////                announceDto.getCreatedBy()
////        );
////    }
