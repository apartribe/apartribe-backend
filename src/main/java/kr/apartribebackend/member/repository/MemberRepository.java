package kr.apartribebackend.member.repository;


import kr.apartribebackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);

    boolean existsByEmail(String Email);

    boolean existsByNickname(String nickname);

}
