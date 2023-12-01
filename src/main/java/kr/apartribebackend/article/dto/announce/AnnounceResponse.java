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
