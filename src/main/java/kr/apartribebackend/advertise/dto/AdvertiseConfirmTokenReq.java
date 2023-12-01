package kr.apartribebackend.advertise.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record AdvertiseConfirmTokenReq(
        @NotEmpty(message = "이름은 공백일 수 없습니다") String name,
        @NotEmpty(message = "이메일은 공백일 수 없습니다") @Email(message = "이메일 형식이 맞지 않습니다") String email,
        @NotEmpty(message = "코드는 공백일 수 없습니다") String code
) {
    public AdvertiseDto toDto() {
        return AdvertiseDto.builder()
                .name(name)
                .email(email)
                .token(code)
                .build();
    }
}
