package kr.apartribebackend.advertise.dto;

import jakarta.validation.constraints.*;
import kr.apartribebackend.advertise.annotation.ValidateNonAuthCode;

@ValidateNonAuthCode
public record AppendAdvertiseReq(
        @NotNull(message = "광고 및 제휴문의를 위한 정보 수집 및 동의 확인란은 체크하셔야 합니다") @AssertTrue Boolean dataCollectAgree,
        @NotEmpty(message = "이름은 공백일 수 없습니다") String name,
        @NotEmpty(message = "이메일은 공백일 수 없습니다") @Email(message = "이메일 형식이 맞지 않습니다") String email,
        String code,
        boolean nonAuth,
        @NotEmpty(message = "제목은 공백일 수 없습니다") @Size(min = 1, max = 60, message = "제목은 60 자 이내여야 합니다") String title,
        @NotEmpty(message = "문의내용은 공백일 수 없습니다") String content
) {
    public AdvertiseDto toDto() {
        return AdvertiseDto.builder()
                .collectData(dataCollectAgree)
                .name(name)
                .email(email)
                .token(code)
                .title(title)
                .content(content)
                .build();
    }
}

