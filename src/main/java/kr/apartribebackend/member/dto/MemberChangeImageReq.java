package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.NotEmpty;

public record MemberChangeImageReq(
        @NotEmpty(message = "프로필 이미지 링크는 공백일 수 없습니다.") String profileImageUrl
) {

}
