package kr.apartribebackend.article.dto.together;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.apartribebackend.article.annotation.LocalDateIsValid;
import kr.apartribebackend.article.domain.RecruitStatus;

import java.time.LocalDate;

// TODO thumbnail 에 url 이 담기기 떄문에 악의적인 문자를 필터링해야한다.
public record AppendTogetherReq(
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
        @NotNull(message = "아파트 주민에게만 공개 여부는 둘 중 하나 선택하셔야합니다.") Boolean onlyApartUser,
        String thumbnail
) {
    public TogetherDto toDto() {
        final RecruitStatus recruitStatus;
        final LocalDate currentDay = LocalDate.now();
        final LocalDate from = LocalDate.parse(recruitFrom);
        final LocalDate to = LocalDate.parse(recruitFrom);

        if (currentDay.isBefore(from)) {
            recruitStatus = RecruitStatus.NOT_YET;
        } else if (currentDay.isAfter(to)) {
            recruitStatus = RecruitStatus.END;
        } else {
            recruitStatus = RecruitStatus.STILL;
        }

        return TogetherDto.builder()
                .title(title)
                .description(description)
                .content(content)
                .recruitFrom(from)
                .recruitTo(to)
                .recruitStatus(recruitStatus)
                .meetTime(meetTime)
                .target(target)
                .location(location)
                .contributeStatus(contributeStatus)
                .thumbnail(thumbnail == null ? "" : thumbnail)
                .onlyApartUser(onlyApartUser)
                .build();
    }
}
