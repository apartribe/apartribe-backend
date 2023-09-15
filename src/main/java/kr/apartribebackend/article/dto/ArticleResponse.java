package kr.apartribebackend.article.dto;


public record ArticleResponse(
        Long id,
        String category,
        int liked,
        int saw,
        String title,
        String content
) {

    public static ArticleResponse from(ArticleDto articleDto) {
        return new ArticleResponse(
                articleDto.getId(),
                articleDto.getCategory().getName(),
                articleDto.getLiked(),
                articleDto.getSaw(),
                articleDto.getTitle(),
                articleDto.getContent()
        );
    }
}
