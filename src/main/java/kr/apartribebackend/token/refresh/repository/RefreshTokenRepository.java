package kr.apartribebackend.token.refresh.repository;


import kr.apartribebackend.token.refresh.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update RefreshToken as r set r.token = :token")
    int updateToken(@Param("token") String token);

}
