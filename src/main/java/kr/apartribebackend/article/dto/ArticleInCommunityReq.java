package kr.apartribebackend.article.dto;

import jakarta.validation.constraints.NotEmpty;

public record ArticleInCommunityReq(
        @NotEmpty(message = "최소한 한글자를 입력해주세요.") String title
) {
}
