package kr.apartribebackend.token.email.domain;

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
@Table(name = "EMAIL_TOKEN")
public class EmailToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMAIL_TOKEN_ID")
    public Long id;

    @Column(name = "TOKEN_VALUE")
    private String value;

    @Column(name = "EXPRIED_AT", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    private EmailToken(Long id,
                       String value,
                       Member member) {
        this.id = id;
        this.value = value;
        this.expiredAt = LocalDateTime.now().plusMinutes(3L);
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailToken that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

    public void confirmEmailToken() {
        this.confirmedAt = LocalDateTime.now();
    }

    public void changeMember(Member member) {
        this.member = member;
    }
}
