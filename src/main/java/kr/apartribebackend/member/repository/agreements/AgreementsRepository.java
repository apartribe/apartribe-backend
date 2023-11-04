package kr.apartribebackend.member.repository.agreements;

import kr.apartribebackend.member.domain.agreements.Agreements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgreementsRepository extends JpaRepository<Agreements, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Agreements as a where a.member.id = :memberId")
    int deleteAgreementsByMemberId(@Param("memberId") Long memberId);

}
