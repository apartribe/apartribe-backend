package kr.apartribebackend.article.dto.together;

import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.article.domain.RecruitStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SingleTogetherResponseProjection {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String thumbnail;
    private int saw;
    private int liked;
    private String createdBy;
    private boolean memberLiked;
    private boolean memberCreated;
    private String profileImage;
    private LocalDateTime createdAt;
    private LocalDate recruitFrom;
    private LocalDate recruitTo;
    private String meetTime;
    private String location;
    private String target;
    private boolean contributeStatus;
    private String recruitStatus;
    private String content;

    @QueryProjection
    public SingleTogetherResponseProjection(Long id,
                                            String category,
                                            String title,
                                            String description,
                                            String thumbnail,
                                            int saw,
                                            int liked,
                                            String createdBy,
                                            boolean memberLiked,
                                            boolean memberCreated,
                                            String profileImage,
                                            LocalDateTime createdAt,
                                            LocalDate recruitFrom,
                                            LocalDate recruitTo,
                                            String meetTime,
                                            String location,
                                            String target,
                                            boolean contributeStatus,
                                            RecruitStatus recruitStatus,
                                            String content) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.saw = saw;
        this.liked = liked;
        this.createdBy = createdBy;
        this.memberLiked = memberLiked;
        this.memberCreated = memberCreated;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.recruitFrom = recruitFrom;
        this.recruitTo = recruitTo;
        this.meetTime = meetTime;
        this.location = location;
        this.target = target;
        this.contributeStatus = contributeStatus;
        this.recruitStatus = recruitStatus.getName();
        this.content = content;
    }
}
