package kr.apartribebackend.oauth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kr.apartribebackend.member.domain.dto.MemberDto;

public record MemberJoinReq(
        @NotBlank @Email String email,
        @NotBlank String code,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$") String password,
        @NotBlank String passwordConfirm,
        @NotBlank String name,
        @NotBlank String nickname
) {

    public MemberDto toDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .build();
    }

}
