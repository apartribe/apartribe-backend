package kr.apartribebackend.apart.domain;


import jakarta.persistence.*;
import kr.apartribebackend.global.domain.BaseEntity;
import kr.apartribebackend.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuperBuilder
@Getter @Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "APARTMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "code", columnNames = "CODE"),
                @UniqueConstraint(name = "name", columnNames = "NAME"),
        }
)
public class Apartment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APART_ID")
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(mappedBy = "apartment")
    private final Set<Member> members = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Apartment apartment)) return false;
        return id != null && Objects.equals(id, apartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /////////////////////////////// BUSINESS LOGIC ///////////////////////////////

//    public void addMember(Member member) {
//        if (member.getApartment() != this) {
//            member.changeApartment(this);
//        }
//        this.members.add(member);
//    }

}
