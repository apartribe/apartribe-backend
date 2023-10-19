package kr.apartribebackend.apart.service;


import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.ApartAlreadyExistsException;
import kr.apartribebackend.apart.exception.ApartMemberDuplicateException;
import kr.apartribebackend.apart.exception.ApartNonExistsException;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public void appendApartment(final MemberDto memberDto,
                                final ApartmentDto apartmentDto,
                                final Member member) {
        if (
                !memberDto.getApartmentDto().getCode().equals("EMPTY") ||
                !memberDto.getApartmentDto().getName().equals("EMPTY")
        ) {
            throw new ApartMemberDuplicateException();
        }
        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
            throw new ApartAlreadyExistsException();
        }
        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
        member.changeApartment(apartment);
    }

    public boolean existsByCode(String apartCode) {
        return apartmentRepository.existsByCode(apartCode);
    }

    public ApartmentDto findApartByCode(String apartCode) {
        return apartmentRepository.findApartmentByCode(apartCode)
                .map(ApartmentDto::from)
                .orElseThrow(ApartNonExistsException::new);
    }
}
