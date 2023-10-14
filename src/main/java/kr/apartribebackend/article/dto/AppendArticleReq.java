package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;


public record AppendArticleReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다") String category,
        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
        @NotEmpty(message = "내용은 공백일 수 없습니다") String content,
        String thumbnail
) {

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .thumbnail(thumbnail == null ? "" : thumbnail)
                .build();
    }
}



//package kr.apartribebackend.article.dto;
//
//import jakarta.validation.constraints.NotEmpty;
//import kr.apartribebackend.article.domain.Category;
//import kr.apartribebackend.article.annotation.IsCategoryValid;
//
//import java.util.Arrays;
//
//public record AppendArticleReq(
//        @NotEmpty(message = "카테고리는 공백일 수 없습니다") @IsCategoryValid String level,
//        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
//        @NotEmpty(message = "내용은 공백일 수 없습니다") String content
//) {
//
//    public ArticleDto toDto() {
//        return ArticleDto.builder()
//                .category(
//                        Arrays.stream(Category.values())
//                                .filter(category -> category.getName().equals(level))
//                                .findFirst().get())
//                .title(title)
//                .content(content)
//                .build();
//    }
//}
