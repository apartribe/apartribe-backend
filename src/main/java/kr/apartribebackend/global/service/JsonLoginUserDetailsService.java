package kr.apartribebackend.global.service;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Slf4j @RequiredArgsConstructor
public class JsonLoginUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Member member = memberRepository.findMemberWithApartInfoByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        final Apartment apartment = member.getApartment();

        final MemberDto memberDto = MemberDto.from(member);
        final ApartmentDto apartmentDto = ApartmentDto.from(apartment);

        final AuthenticatedMember authenticatedMember = AuthenticatedMember.from(memberDto, apartmentDto);
        authenticatedMember.setOriginalEntity(member);
        return authenticatedMember;
    }
}
