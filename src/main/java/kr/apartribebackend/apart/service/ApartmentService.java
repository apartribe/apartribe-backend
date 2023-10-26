package kr.apartribebackend.apart.service;


import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.apart.exception.*;
import kr.apartribebackend.apart.repository.ApartmentRepository;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    public void authenticateApartment(final MemberDto memberDto,
                                      final ApartmentDto apartmentDto,
                                      final Member member) {
        checkMemberHaveApartments(member);                                          // Member 가 Apart 에 등록되어있으면 Exception
        if (hasText(member.getApartCode()) || hasText(member.getApartName())) {     // Member 가 Apart 인증이 되어있으면 Exception
            throw new AlreadyAuthenticateApartException();
        }
        final Apartment findApartment = apartmentRepository.findApartmentByCode(apartmentDto.getCode())
                .orElse(null);
        if (findApartment != null) {                                                // 인증한 Apart 가 존재하면 Member 에 Apart 를 등록하고, AUTHSTATUS 를 COMPLETED 로 설정
            log.info("인증하신 아파트는 사이트에 이미 존재하니, 사용자를 아파트에 등록하고, COMPLETED 로 처리합니다.");
            member.changeApartment(findApartment);
            member.authenticateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
        } else {                                                                    // 인증한 Apart 가 존재하지 않으면 AUTHSTATUS 를 PENDING 으로 설정
            log.info("인증하신 아파트는 사이트에 존재하지 않으니, PENDING 으로 처리합니다.");
            member.pendingApartInfo(apartmentDto.getCode(), apartmentDto.getName());
        }
    }

    public void constructCommunity(final MemberDto memberDto,
                                   final ApartmentDto apartmentDto,
                                   final Member member) {
        checkMemberHaveApartments(member);                                          // Member 가 Apart 에 등록되어있으면 Exception
        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
            throw new ApartAlreadyExistsException();                                // 만들으려는 아파트 커뮤니티가 존재하면 Exception
        }
        if (                                                                        // 만들으려는 아파트 커뮤니티와 사전에 인증한 아파트 정보와 일치하지 않으면 Exception
                !member.getApartCode().equals(apartmentDto.getCode()) ||
                !member.getApartName().equals(apartmentDto.getName())
        ) {
            throw new ApartmentMemberIntegrityException();
        }
        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
        member.changeApartment(apartment);
        member.authenticateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
    }

    public boolean existsByCode(String apartCode) {
        return apartmentRepository.existsByCode(apartCode);
    }

    public ApartmentDto findApartByCode(String apartCode) {
        return apartmentRepository.findApartmentByCode(apartCode)
                .map(ApartmentDto::from)
                .orElseThrow(ApartNonExistsException::new);
    }

    private void checkMemberHaveApartments(Member member) {
        if (member.getApartment() != null) {
            throw new ApartMemberDuplicateException();
        }
    }

}



//package kr.apartribebackend.apart.service;
//
//
//import kr.apartribebackend.apart.domain.Apartment;
//import kr.apartribebackend.apart.dto.ApartmentDto;
//import kr.apartribebackend.apart.exception.*;
//import kr.apartribebackend.apart.repository.ApartmentRepository;
//import kr.apartribebackend.member.domain.Member;
//import kr.apartribebackend.member.dto.MemberDto;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import static org.springframework.util.StringUtils.*;
//
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class ApartmentService {
//
//    private final ApartmentRepository apartmentRepository;
//
//    public void authenticateApartment(final MemberDto memberDto,
//                                      final ApartmentDto apartmentDto,
//                                      final Member member) {
//        checkMemberHaveApartments(memberDto);
//        if (hasText(member.getApartCode()) || hasText(member.getApartName())) {
//            throw new AlreadyAuthenticateApartException();
//        }
//        final Apartment findApartment = apartmentRepository.findApartmentByCode(apartmentDto.getCode())
//                .orElse(null);
//        if (findApartment != null) {
//            member.changeApartment(findApartment);
//            member.updateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
//        } else {
//            member.rememberApartInfo(apartmentDto.getCode(), apartmentDto.getName());
//        }
//    }
//
//    public void constructCommunity(final MemberDto memberDto,
//                                   final ApartmentDto apartmentDto,
//                                   final Member member) {
//        checkMemberHaveApartments(memberDto);
//        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
//            throw new ApartAlreadyExistsException();
//        }
//        if (
//                !member.getApartCode().equals(apartmentDto.getCode()) ||
//                !member.getApartName().equals(apartmentDto.getName())
//        ) {
//            throw new ApartmentMemberIntegrityException();
//        }
//        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
//        member.changeApartment(apartment);
//        member.updateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
//    }
//
//    public boolean existsByCode(String apartCode) {
//        return apartmentRepository.existsByCode(apartCode);
//    }
//
//    public ApartmentDto findApartByCode(String apartCode) {
//        return apartmentRepository.findApartmentByCode(apartCode)
//                .map(ApartmentDto::from)
//                .orElseThrow(ApartNonExistsException::new);
//    }
//
//    private void checkMemberHaveApartments(MemberDto memberDto) {
//        if (
//                !memberDto.getApartmentDto().getCode().equals("EMPTY") ||
//                !memberDto.getApartmentDto().getName().equals("EMPTY")
//        ) {
//            throw new ApartMemberDuplicateException();
//        }
//    }
//}


//    public void authenticateApartment(final MemberDto memberDto,
//                                      final ApartmentDto apartmentDto,
//                                      final Member member) {
//        checkMemberHaveApartments(memberDto);
//        if (
//                StringUtils.hasText(member.getApartCode()) ||
//                StringUtils.hasText(member.getApartName())
//        ) {
//            throw new AlreadyAuthenticateApartException();
//        }
//        member.rememberApartInfo(apartmentDto.getCode(), apartmentDto.getName());
//    }

//    public void constructCommunity(final MemberDto memberDto,
//                                   final ApartmentDto apartmentDto,
//                                   final Member member) {
//        checkMemberHaveApartments(memberDto);
//        if (apartmentRepository.existsByCodeAndName(apartmentDto.getCode(), apartmentDto.getName())) {
//            throw new ApartAlreadyExistsException();
//        }
//        if (
//                !member.getApartCode().equals(apartmentDto.getCode()) ||
//                        !member.getApartName().equals(apartmentDto.getName())
//        ) {
//            throw new ApartmentMemberIntegrityException();
//        }
//        final Apartment apartment = apartmentRepository.save(apartmentDto.toEntity());
//        member.changeApartment(apartment);
//        member.updateApartInfo(apartmentDto.getCode(), apartmentDto.getName());
//    }