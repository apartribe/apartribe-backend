package kr.apartribebackend.apart.service;


import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.ApartAlreadyExistsException;
import kr.apartribebackend.apart.exception.ApartMemberDuplicateException;
import kr.apartribebackend.apart.exception.ApartNonExistsException;
import kr.apartribebackend.apart.exception.ApartmentMemberIntegrityException;
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

    public void authenticateApartment(final MemberDto memberDto,
                                      final ApartmentDto apartmentDto,
                                      final Member member) {
        checkMemberHaveApartments(memberDto);
        // TODO 원래라면 여기에 아파트를 인증하는 코드를 작성하여야 한다. 하지만 지금은 방법이 없으므로 pass 한다.
        member.rememberApartInfo(apartmentDto.getCode(), apartmentDto.getName());
    }

    public void constructCommunity(final MemberDto memberDto,
                                   final ApartmentDto apartmentDto,
                                   final Member member) {
        checkMemberHaveApartments(memberDto);
        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
            throw new ApartAlreadyExistsException();
        }
        if (
                !member.getApartCode().equals(apartmentDto.getCode()) ||
                !member.getApartName().equals(apartmentDto.getName())
        ) {
            throw new ApartmentMemberIntegrityException();
        }
        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
        member.changeApartment(apartment);
        member.updateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
    }

    public boolean existsByCode(String apartCode) {
        return apartmentRepository.existsByCode(apartCode);
    }

    public ApartmentDto findApartByCode(String apartCode) {
        return apartmentRepository.findApartmentByCode(apartCode)
                .map(ApartmentDto::from)
                .orElseThrow(ApartNonExistsException::new);
    }

    private void checkMemberHaveApartments(MemberDto memberDto) {
        if (
                !memberDto.getApartmentDto().getCode().equals("EMPTY") ||
                        !memberDto.getApartmentDto().getName().equals("EMPTY")
        ) {
            throw new ApartMemberDuplicateException();
        }
    }
}
