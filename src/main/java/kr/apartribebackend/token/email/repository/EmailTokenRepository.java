package kr.apartribebackend.token.email.repository;

import kr.apartribebackend.token.email.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    boolean existsByValue(String value);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from EmailToken as et where et.member.id = :memberId")
    int deleteEmailTokenByMemberId(@Param("memberId") Long memberId);

}
