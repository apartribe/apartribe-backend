package kr.apartribebackend.token.email.repository;

import kr.apartribebackend.token.email.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    boolean existsByValue(String value);

}
