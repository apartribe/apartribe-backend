package kr.apartribebackend.member.dto.forgot;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record ForgotReq(
        @NotEmpty(message = "이메일은 공백일 수 없습니다.") @Email(message = "이메일 형식이 맞지 않습니다") String email,
        @NotEmpty(message = "이름은 공백일 수 없습니다.") String name
) { }
