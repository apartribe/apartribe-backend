package kr.apartribebackend.member.dto;

import jakarta.validation.constraints.*;
import kr.apartribebackend.member.dto.agreements.AgreementsDto;
import org.springframework.util.StringUtils;

// TODO profileImageUrl 악의적인 문자를 필터링해야 한다.
public record MemberJoinReq(
        @NotEmpty(message = "이메일은 공백일 수 없습니다") @Email(message = "이메일 형식이 맞지 않습니다") String email,
        @NotEmpty(message = "인증코드는 공백일 수 없습니다") String code,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$", message = "비밀번호는 영문, 숫자, 특수문자 혼합 8~20 자여야 합니다") String password,
        @NotEmpty(message = "비밀번호확인은 공백일 수 없습니다") String passwordConfirm,
        @NotEmpty(message = "이름은 공백일 수 없습니다") String name,
        @NotEmpty(message = "닉네임은 공백일 수 없습니다") String nickname,
        String profileImageUrl,
        @NotNull(message = "만 14 세 이상입니다 확인란은 체크하셔야 합니다.") @AssertTrue Boolean goeFourteen,
        @NotNull(message = "저작권 안내 확인란은 체크하셔야 합니다.") @AssertTrue Boolean confirmCopyright,
        @NotNull(message = "이용약관동의 확인란은 체크하셔야 합니다.") @AssertTrue Boolean useAgree,
        @NotNull(message = "개인정보 수집 및 이용 동의 확인란은 체크하셔야 합니다.") @AssertTrue Boolean dataCollectAgree,
        @NotNull(message = "홍보 및 마케팅 이용 동의 확인란은 체크하셔야 합니다.") Boolean advertiseAgree
) {

    public MemberDto toMemberDto() {
        return MemberDto.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl != null ? StringUtils.cleanPath(profileImageUrl) : "")
                .build();
    }

    public AgreementsDto toAgreementsDto() {
        return AgreementsDto.builder()
                .goeFourteen(goeFourteen)
                .confirmCopyright(confirmCopyright)
                .useAgree(useAgree)
                .dataCollectAgree(dataCollectAgree)
                .advertiseAgree(advertiseAgree)
                .build();
    }
}
