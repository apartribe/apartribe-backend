package kr.apartribebackend.member.repository.forgot;

import kr.apartribebackend.member.domain.forgot.Forgot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ForgotRepository extends JpaRepository<Forgot, Long> {

    @Query(value = "select f from Forgot as f inner join fetch f.member as m where f.identifier = :identifier")
    Optional<Forgot> findForgotWithMemberByIdentifier(@Param("identifier") String identifier);

    @Query(value = "select f from Forgot as f where f.member.id = :memberId")
    Optional<Forgot> findForgotByMemberId(@Param("memberId") Long memberId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update Forgot as f set f.expiredAt = :expiredAt, f.identifier = :identifier where f.id = :forgotId")
    int updateForgotLinkByMemberId(@Param("expiredAt") LocalDateTime expiredAt,
                                   @Param("identifier") String identifier,
                                   @Param("forgotId") Long forgotId);

}
