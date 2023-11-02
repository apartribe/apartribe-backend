package kr.apartribebackend.article.dto.together;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.apartribebackend.article.annotation.IsRecruitStatusValid;
import kr.apartribebackend.article.annotation.LocalDateIsValid;
import kr.apartribebackend.article.domain.RecruitStatus;

import java.time.LocalDate;
import java.util.Arrays;

// TODO thumbnail 에 url 이 담기기 떄문에 악의적인 문자를 필터링해야한다.
public record UpdateTogetherReq(
        @NotEmpty(message = "카테고리는 공백일 수 없습니다.") String category,
        @NotEmpty(message = "제목은 공백일 수 없습니다.") String title,
        @NotEmpty(message = "설명은 공백일 수 없습니다.") @Size(min = 1, max = 60, message = "설명은 20 자 이하여야 합니다.") String description,
        @NotEmpty(message = "내용은 공백일 수 없습니다.") String content,
        @NotEmpty(message = "모집 시작일은 공백일 수 없습니다.") @LocalDateIsValid String recruitFrom,
        @NotEmpty(message = "모집 종료일은 공백일 수 없습니다.") @LocalDateIsValid String recruitTo,
        @NotEmpty(message = "활동 시간은 공백일 수 없습니다.") String meetTime,
        @NotEmpty(message = "모집 대상은 공백일 수 없습니다.") String target,
        @NotEmpty(message = "활동 장소는 공백일 수 없습니다.") String location,
        @NotNull(message = "회비여부는 true 혹은 false 여야 합니다.") Boolean contributeStatus,
        @NotEmpty(message = "모집 상태는 공백일 수 없습니다.") @IsRecruitStatusValid String recruitStatus,
        @NotEmpty(message = "모집 상태는 공백일 수 없습니다.") String thumbnail
) {
    public TogetherDto toDto() {
        return TogetherDto.builder()
                .title(title)
                .description(description)
                .content(content)
                .recruitFrom(LocalDate.parse(recruitFrom))
                .recruitTo(LocalDate.parse(recruitTo))
                .recruitStatus(
                        Arrays.stream(RecruitStatus.values())
                                .filter(status -> status.getName().equals(recruitStatus))
                                .findFirst()
                                .get()
                )
                .meetTime(meetTime)
                .target(target)
                .location(location)
                .contributeStatus(contributeStatus)
                .thumbnail(thumbnail == null ? "" : thumbnail)
                .build();
    }
}