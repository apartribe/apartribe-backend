package kr.apartribebackend.apart.service;


import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.ApartAlreadyExistsException;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public void appendApartment(ApartmentDto apartmentDto, Member member) {
        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName()))
            throw new ApartAlreadyExistsException();
        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
        member.changeApartment(apartment);
    }
}
