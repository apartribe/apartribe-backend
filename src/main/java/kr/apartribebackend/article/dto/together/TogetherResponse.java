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


//package kr.apartribebackend.article.dto.together;
//
//import kr.apartribebackend.article.domain.Together;
//
//import java.time.LocalDateTime;
//
//public record TogetherResponse(
//        Long id,
//        String category,
//        String recruitStatus,
//        String createdBy,
//        String title,
//        String thumbnail,
//        String description,
//        LocalDateTime createdAt){
//    public static TogetherResponse from(Together together) {
//        return new TogetherResponse(
//                together.getId(),
//                together.getCategory().getName(),
//                together.getRecruitStatus().getName(),
//                together.getCreatedBy(),
//                together.getTitle(),
//                together.getThumbnail(),
//                together.getDescription(),
//                together.getCreatedAt()
//        );
//    }
//}
