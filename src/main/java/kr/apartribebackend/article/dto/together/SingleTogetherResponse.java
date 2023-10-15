package kr.apartribebackend.article.dto.together;

import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.dto.SingleCommentResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SingleTogetherResponse(
        Long id,
        String category,
        String title,
        int saw,
        int liked,
        long commentCounts,
        String createdBy,
        LocalDateTime createdAt,
        LocalDate recruitFrom,
        LocalDate recruitTo,
        String meetTime,
        String location,
        String target,
        boolean contributeStatus,
        String recruitStatus,
        String content,
        String thumbnail
) {

    public static SingleTogetherResponse from(Together together) {
        together.reflectArticleSaw();
        List<SingleCommentResponse> commentResponses = together.getComments()
                .stream()
                .map(SingleCommentResponse::from)
                .toList();
        return new SingleTogetherResponse(
                together.getId(),
                together.getCategory().getName(),
                together.getTitle(),
                together.getSaw(),
                together.getLiked(),
                commentResponses.size(),
                together.getCreatedBy(),
                together.getCreatedAt(),
                together.getRecruitFrom(),
                together.getRecruitTo(),
                together.getMeetTime(),
                together.getLocation(),
                together.getTarget(),
                together.isContributeStatus(),
                together.getRecruitStatus().getName(),
                together.getContent(),
                together.getThumbnail()
        );
    }

}
