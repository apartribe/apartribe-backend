package kr.apartribebackend.article.dto.announce;

import kr.apartribebackend.article.domain.Announce;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AnnounceDto {

    private Long id;
    private String title;
    private String content;
    private int liked;
    private int saw;
    private Level level;
    private Member member;
    private LocalDateTime createdAt;
    private String createdBy;
    private String thumbnail;
    private LocalDate floatFrom;
    private LocalDate floatTo;
    private boolean onlyApartUser;

    @Builder
    private AnnounceDto(Long id,
                        String title,
                        String content,
                        int liked,
                        int saw,
                        Level level,
                        Member member,
                        LocalDateTime createdAt,
                        String createdBy,
                        String thumbnail,
                        LocalDate floatFrom,
                        LocalDate floatTo,
                        boolean onlyApartUser) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.saw = saw;
        this.level = level;
        this.member = member;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.thumbnail = thumbnail;
        this.floatFrom = floatFrom;
        this.floatTo = floatTo;
        this.onlyApartUser = onlyApartUser;
    }

    public static AnnounceDto from(Announce announce) {
        return AnnounceDto.builder()
                .id(announce.getId())
                .title(announce.getTitle())
                .content(announce.getContent())
                .liked(announce.getLiked())
                .saw(announce.getSaw())
                .level(announce.getLevel())
                .member(announce.getMember())
                .createdAt(announce.getCreatedAt())
                .createdBy(announce.getCreatedBy())
                .thumbnail(announce.getThumbnail())
                .floatFrom(announce.getFloatFrom())
                .floatTo(announce.getFloatFrom())
                .onlyApartUser(announce.isOnlyApartUser())
                .build();
    }

    public Announce toEntity(Member member) {
        return Announce.builder()
                .id(id)
                .title(title)
                .content(content)
                .liked(liked)
                .saw(saw)
                .level(level)
                .member(member)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .thumbnail(thumbnail)
                .floatFrom(floatFrom)
                .floatTo(floatTo)
                .onlyApartUser(onlyApartUser)
                .build();
    }
}
