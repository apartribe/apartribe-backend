package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateReq(
        @NotEmpty(message = "이메일은 공백일 수 없습니다") @Email(message = "이메일 형식이 맞지 않습니다") String email,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자 혼합 8~20 자여야 합니다") String password,
        @NotEmpty(message = "비밀번호확인은 공백일 수 없습니다") String passwordConfirm,
        @NotEmpty(message = "이름은 공백일 수 없습니다") String name,
        @NotEmpty(message = "닉네임은 공백일 수 없습니다") String nickname
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
