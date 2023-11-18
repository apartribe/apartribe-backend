package kr.apartribebackend.apart.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.apartribebackend.member.annotation.IsPositionValid;
import kr.apartribebackend.member.annotation.IsUserTypeValid;

public record AuthenticateApartmentReq(
        @NotEmpty(message = "code 는 꼭 명시해야 합니다.") String code,
        @NotEmpty(message = "name 는 꼭 명시해야 합니다.") String name,
        @NotNull(message = "userType 은 꼭 명시해야 합니다.") @IsUserTypeValid String userType,
        @NotNull(message = "position 은 꼭 명시해야 합니다.") @IsPositionValid String position
) {
    public ApartmentDto toDto() {
        return ApartmentDto.builder()
                .code(code)
                .name(name)
                .build();
    }
}
