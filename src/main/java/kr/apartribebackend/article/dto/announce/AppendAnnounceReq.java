package kr.apartribebackend.article.dto.announce;

import jakarta.validation.constraints.NotEmpty;
import kr.apartribebackend.article.domain.Level;
import kr.apartribebackend.article.annotation.IsLevelValid;

import java.util.Arrays;

// TODO thumbnail 에 url 이 담기기 떄문에 악의적인 문자를 필터링해야한다.
public record AppendAnnounceReq(
        @NotEmpty(message = "공지사항 레벨은 공백일 수 없습니다") @IsLevelValid String level,
        @NotEmpty(message = "제목은 공백일 수 없습니다") String title,
        @NotEmpty(message = "내용은 공백일 수 없습니다") String content,
        String thumbnail
) {

    public AnnounceDto toDto() {
        return AnnounceDto.builder()
                .level(
                        Arrays.stream(Level.values())
                                .filter(l -> l.getName().equals(level))
                                .findFirst().get())
                .title(title)
                .content(content)
                .thumbnail(thumbnail == null ? "" : thumbnail)
                .build();
    }
}
