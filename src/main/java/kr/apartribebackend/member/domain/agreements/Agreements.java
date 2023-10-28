package kr.apartribebackend.member.domain.agreements;


import jakarta.persistence.*;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Table(name = "AGREEMENTS")
public class Agreements {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AGREEMENTS_ID")
    private Long id;

    @Column(name = "GOE_FOURTEEN")
    private boolean goeFourteen;

    @Column(name = "CONFIRM_C")
    private boolean confirmCopyright;

    @Column(name = "USE_AGREE")
    private boolean useAgree;

    @Column(name = "DATA_COLLECT_AGREE")
    private boolean dataCollectAgree;

    @Column(name = "AD_AGREE")
    private boolean advertiseAgree;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreements that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
