package kr.apartribebackend.article.dto.together;

import com.querydsl.core.annotations.QueryProjection;
import kr.apartribebackend.article.domain.RecruitStatus;
import kr.apartribebackend.category.domain.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TogetherResponse {

    private Long id;
    private String category;
    private String recruitStatus;
    private String createdBy;
    private String title;
    private String thumbnail;
    private String description;
    private LocalDateTime createdAt;
    private boolean onlyApartUser;

    @QueryProjection
    public TogetherResponse(Long id,
                            Category category,
                            RecruitStatus recruitStatus,
                            String createdBy,
                            String title,
                            String thumbnail,
                            String description,
                            LocalDateTime createdAt,
                            boolean onlyApartUser) {
        this.id = id;
        this.category = category.getName();
        this.recruitStatus = recruitStatus.getName();
        this.createdBy = createdBy;
        this.title = title;
        this.thumbnail = thumbnail;
        this.description = description;
        this.createdAt = createdAt;
        this.onlyApartUser = onlyApartUser;
    }
}
