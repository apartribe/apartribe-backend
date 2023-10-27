package kr.apartribebackend.advertise.repository;

import kr.apartribebackend.advertise.domain.Advertise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertiseRepository extends JpaRepository<Advertise, Long> {

    Optional<Advertise> findByEmailAndName(String email, String name);

    Optional<Advertise> findByEmailAndNameAndToken(String email, String name, String token);

}
