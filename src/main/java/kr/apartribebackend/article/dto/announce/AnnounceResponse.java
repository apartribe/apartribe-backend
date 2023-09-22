package kr.apartribebackend.article.dto.announce;


import java.time.LocalDateTime;

public record AnnounceResponse(
        Long id,
        String level,
        int liked,
        int saw,
        String title,
        String content,
        LocalDateTime createdAt,
        String createdBy
) {

    public static AnnounceResponse from(AnnounceDto announceDto) {
        return new AnnounceResponse(
                announceDto.getId(),
                announceDto.getLevel().getName(),
                announceDto.getLiked(),
                announceDto.getSaw(),
                announceDto.getTitle(),
                announceDto.getContent(),
                announceDto.getCreatedAt(),
                announceDto.getCreatedBy()
        );
    }
}
