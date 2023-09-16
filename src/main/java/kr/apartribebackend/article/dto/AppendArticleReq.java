package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;
import kr.apartribebackend.article.domain.Category;
import kr.apartribebackend.global.annotation.IsCategoryValid;

import java.util.Arrays;

// TODO 나중에 첨부파일도 추가하여야 함.
public record AppendArticleReq(
        @NotEmpty(message = "이메일은 공백일 수 없습니다") @IsCategoryValid String level,
        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
        @NotEmpty(message = "상세정보는 공백일 수 없습니다") String content
) {

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .category(
                        Arrays.stream(Category.values())
                                .filter(category -> category.getName().equals(level))
                                .findFirst().get())
                .title(title)
                .content(content)
                .build();
    }
}
