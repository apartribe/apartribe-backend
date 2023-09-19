package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record MemberChangePasswordReq(
        @NotEmpty(message = "기존 비밀번호란은 공백일 수 없습니다.") String currentPassword,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자 혼합 8~20 자여야 합니다") String newPassword,
        @NotEmpty(message = "비밀번호 확인란은 공백일 수 없습니다") String passwordConfirm
) {

}
