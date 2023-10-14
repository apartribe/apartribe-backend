package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;

public record UpdateArticleReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다") String category,
        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
        @NotEmpty(message = "내용은 공백일 수 없습니다") String content
) {

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .build();
    }
}