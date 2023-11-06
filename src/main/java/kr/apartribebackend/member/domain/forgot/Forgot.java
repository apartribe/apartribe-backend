package kr.apartribebackend.member.domain.forgot;

import jakarta.persistence.*;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FORGOT")
public class Forgot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FORGOT_ID")
    private Long id;

    @Column(name = "IDENTIFIER")
    private String identifier;

    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NAME")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Forgot forgot)) return false;
        return id != null && Objects.equals(id, forgot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Builder
    private Forgot(Long id, String identifier, LocalDateTime expiredAt, String email, String name, Member member) {
        this.id = id;
        this.identifier = identifier;
        this.expiredAt = expiredAt != null ? expiredAt : LocalDateTime.now().plusMinutes(3);
        this.email = email;
        this.name = name;
        this.member = member;
    }
}
