package kr.apartribebackend.apart.repository;

import kr.apartribebackend.apart.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    boolean existsByCodeAndName(String code, String name);

}
