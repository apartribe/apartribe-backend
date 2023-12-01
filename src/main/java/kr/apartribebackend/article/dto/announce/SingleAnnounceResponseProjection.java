package kr.apartribebackend.article.dto.announce;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.member.domain.Position;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SingleAnnounceResponseProjection {
    private Long id;
    private String createdBy;
    private String profileImage;
    private String thumbnail;
    private LocalDateTime createdAt;
    private String level;
    private String title;
    private String content;
    private LocalDate floatFrom;
    private LocalDate floatTo;
    private int liked;
    private boolean memberLiked;
    private boolean memberCreated;
    private boolean onlyApartUser;
    private int saw;
    private String position;
    @JsonIgnore private String apartCode;

    @QueryProjection
    public SingleAnnounceResponseProjection(Long id,
                                            String createdBy,
                                            String profileImage,
                                            String thumbnail,
                                            LocalDateTime createdAt,
                                            Level level,
                                            String title,
                                            String content,
                                            LocalDate floatFrom,
                                            LocalDate floatTo,
                                            int liked,
                                            boolean memberLiked,
                                            boolean memberCreated,
                                            boolean onlyApartUser,
                                            int saw,
                                            Position position,
                                            String apartCode) {
        this.id = id;
        this.createdBy = createdBy;
        this.profileImage = profileImage;
        this.thumbnail = thumbnail;
        this.createdAt = createdAt;
        this.level = level.getName();
        this.title = title;
        this.content = content;
        this.floatFrom = floatFrom;
        this.floatTo = floatTo;
        this.liked = liked;
        this.memberLiked = memberLiked;
        this.memberCreated = memberCreated;
        this.onlyApartUser = onlyApartUser;
        this.saw = saw;
        this.position = position.getName();
        this.apartCode = apartCode;
    }
}
