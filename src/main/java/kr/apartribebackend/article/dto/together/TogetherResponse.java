package kr.apartribebackend.article.dto.together;

import kr.apartribebackend.article.domain.Together;

import java.time.LocalDateTime;

public record TogetherResponse(
        Long id,
        String category,
        String recruitStatus,
        String createdBy,
        String title,
        String thumbnail,
        String description,
        LocalDateTime createdAt){
    public static TogetherResponse from(Together together) {
        return new TogetherResponse(
                together.getId(),
                together.getCategory().getName(),
                together.getRecruitStatus().getName(),
                together.getCreatedBy(),
                together.getTitle(),
                together.getThumbnail(),
                together.getDescription(),
                together.getCreatedAt()
        );
    }
}
