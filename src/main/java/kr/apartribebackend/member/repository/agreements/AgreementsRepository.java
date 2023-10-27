package kr.apartribebackend.member.repository.agreements;

import kr.apartribebackend.member.domain.agreements.Agreements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementsRepository extends JpaRepository<Agreements, Long> {

}
