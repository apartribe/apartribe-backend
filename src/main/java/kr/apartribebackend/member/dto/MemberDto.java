package kr.apartribebackend.member.dto;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private RefreshToken refreshToken;
    private Apartment apartment;

    @Builder
    private MemberDto(Long id,
                      String email,
                      String password,
                      String name,
                      String nickname,
                      String profileImageUrl,
                      RefreshToken refreshToken,
                      Apartment apartment) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
        this.apartment = apartment;
    }

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public Member toEntity(RefreshToken refreshToken) {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .refreshToken(refreshToken)
                .build();
    }

}
