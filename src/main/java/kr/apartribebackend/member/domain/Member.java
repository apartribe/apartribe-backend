package kr.apartribebackend.member.domain;

import jakarta.persistence.*;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.*;

import java.util.Objects;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "MEMBER",
        uniqueConstraints = {
                @UniqueConstraint(name = "nickname", columnNames = "NICKNAME"),
                @UniqueConstraint(name = "email", columnNames = "EMAIL"),
        }
)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    public Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "PROFILE_IMAGE")
    private String profileImageUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "REFRESH_TOKEN_ID")
    private RefreshToken refreshToken;

    @Builder
    private Member(Long id,
                   String email,
                   String password,
                   String name,
                   String nickname,
                   String profileImageUrl,
                   RefreshToken refreshToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateMemberInfo(String email,
                                 String password,
                                 String name,
                                 String nickname,
                                 String profileImageUrl) {
         this.email = email;
         this.password = password;
         this.name = name;
         this.nickname = nickname;
         this.profileImageUrl = profileImageUrl;
    }

    public void changeRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void changeRefreshTokenValue(RefreshToken refreshToken) {
        this.refreshToken.updateTokenValue(refreshToken.getToken());
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

}
