package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record MemberChangeNicknameReq(
        @NotEmpty(message = "닉네임은 공백일 수 없습니다.") String nickname
) {

}
