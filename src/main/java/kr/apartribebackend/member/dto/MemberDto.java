package kr.apartribebackend.member.dto;

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

    @Builder
    private MemberDto(Long id,
                      String email,
                      String password,
                      String name,
                      String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
    }

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .nickname(member.getNickname())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .build();
    }

    public Member toEntity(RefreshToken refreshToken) {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .refreshToken(refreshToken)
                .build();
    }

}
