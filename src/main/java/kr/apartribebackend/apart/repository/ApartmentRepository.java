package kr.apartribebackend.apart.repository;

import kr.apartribebackend.apart.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    boolean existsByCodeAndName(String code, String name);

    boolean existsByCode(String code);

    Optional<Apartment> findApartmentByCode(String code);

}
