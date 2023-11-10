package kr.apartribebackend.token.email.repository;

import kr.apartribebackend.token.email.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    boolean existsByValue(String value);

    boolean existsByEmailAndMemberNotNull(String email);

    EmailToken findEmailTokenByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from EmailToken as et where et.member.id = :memberId")
    int deleteEmailTokenByMemberId(@Param("memberId") Long memberId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update EmailToken as et set" +
            " et.value = :tokenValue, et.expiredAt = :expiredAt, et.confirmedAt = null" +
            " where et.id = :emailTokenId")
    int updateEmailTokenValue(@Param("tokenValue") String tokenValue,
                              @Param("expiredAt") LocalDateTime expiredAt,
                              @Param("emailTokenId") Long emailTokenId);

}
