package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// TODO thumbnail 에 url 이 담기기 떄문에 악의적인 문자를 필터링해야한다.
public record AppendArticleReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다") String category,
        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
        @NotEmpty(message = "내용은 공백일 수 없습니다") String content,
        @NotNull(message = "아파트 주민에게만 공개 여부는 둘 중 하나 선택하셔야합니다.") Boolean onlyApartUser,
        String thumbnail
) {
    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .thumbnail(thumbnail == null ? "" : thumbnail)
                .onlyApartUser(onlyApartUser)
                .build();
    }
}
