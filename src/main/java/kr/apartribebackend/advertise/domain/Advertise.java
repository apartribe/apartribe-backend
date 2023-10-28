package kr.apartribebackend.advertise.domain;

import jakarta.persistence.*;
import kr.apartribebackend.global.domain.TimeBaseEntity;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;


@SuperBuilder @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Table(name = "ADVERTISE")
public class Advertise extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADVERTISE_ID")
    private Long id;

    @Column(name = "COLLECT_DATA_AGREE")
    private boolean collectDataAgree;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT", columnDefinition="TEXT")
    private String content;

    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "TOKEN_STATUS", nullable = false)
    private AdvertiseTokenStatus advertiseTokenStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Advertise advertise)) return false;
        return id != null && Objects.equals(id, advertise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateTokenValue(String token) {
        this.expiredAt = LocalDateTime.now().plusMinutes(3);
        this.token = token;
    }

    public void authenticateToken() {
        this.advertiseTokenStatus = AdvertiseTokenStatus.COMPLETED;
    }

    public void updateAdvertise(boolean collectDataAgree, String title, String content) {
        this.collectDataAgree = collectDataAgree;
        this.title = title;
        this.content = content;
    }

}
