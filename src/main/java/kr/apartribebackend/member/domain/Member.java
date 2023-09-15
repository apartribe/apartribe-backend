package kr.apartribebackend.member.domain;

import jakarta.persistence.*;
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

    @Builder
    private Member(Long id,
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
                                 String nickname) {
         this.email = email;
         this.password = password;
         this.name = name;
         this.nickname = nickname;
    }

}