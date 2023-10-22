package kr.apartribebackend.article.dto.announce;

import jakarta.validation.constraints.NotEmpty;
import kr.apartribebackend.article.annotation.LocalDateIsValid;

import java.time.LocalDate;

public record AnnounceWidgetDuration(
        @NotEmpty(message = "위젯 시작 시간은 공백일 수 없습니다.") @LocalDateIsValid String floatFrom,
        @NotEmpty(message = "위젯 종료 떠있을 시간은 공백일 수 없습니다.") @LocalDateIsValid String floatTo
) {
    public AnnounceDto toDto() {
        return AnnounceDto.builder()
                .floatFrom(LocalDate.parse(floatFrom))
                .floatTo(LocalDate.parse(floatTo))
                .build();
    }
}
