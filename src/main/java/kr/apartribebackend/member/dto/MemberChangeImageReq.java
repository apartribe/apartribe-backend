package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

// TODO profileImageUrl 악의적인 문자를 필터링해야 한다.
public record MemberChangeImageReq(
        @NotEmpty(message = "프로필 이미지 링크는 공백일 수 없습니다.") String profileImageUrl
) {

}
