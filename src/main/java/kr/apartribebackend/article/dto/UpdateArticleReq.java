package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

public record UpdateArticleReq(
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
                .onlyApartUser(onlyApartUser)
                .thumbnail(StringUtils.cleanPath(thumbnail))
                .build();
    }
}