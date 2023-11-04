package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SingleAnnounceResponse(
        Long id,
        String createdBy,
        String profileImage,
        String thumbnail,
        LocalDateTime createdAt,
        String level,
        String title,
        String content,
        LocalDate floatFrom,
        LocalDate floatTo,
        int liked,
        int saw,
        boolean onlyApartUser
) {
    public static SingleAnnounceResponse from(Announce announce, Member member) {
        announce.reflectArticleSaw();
        return new SingleAnnounceResponse(
                announce.getId(),
                announce.getCreatedBy(),
                member.getProfileImageUrl(),
                announce.getThumbnail(),
                announce.getCreatedAt(),
                announce.getLevel().getName(),
                announce.getTitle(),
                announce.getContent(),
                announce.getFloatFrom(),
                announce.getFloatTo(),
                announce.getLiked(),
                announce.getSaw(),
                announce.isOnlyApartUser()
        );
    }
}
