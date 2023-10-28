package kr.apartribebackend.member.service;

import kr.apartribebackend.apart.domain.Apartment;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.SingleMemberResponse;
import kr.apartribebackend.member.exception.*;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public SingleMemberResponse findMemberWithApartInfoByEmail(final String email) {
        return memberRepository.findMemberWithApartInfoByEmail(email)
                .map(SingleMemberResponse::from)
                .orElseThrow(UserNotFoundException::new);
    }

    public void updateSingleMember(final MemberDto memberDto, final String email) {

        final Member newMemberInfo = memberDto.toEntity();
        memberRepository.findByEmail(email)
                .ifPresentOrElse(member -> member
                        .updateMemberInfo(
                                newMemberInfo.getEmail(),
                                newMemberInfo.getPassword(),
                                newMemberInfo.getName(),
                                newMemberInfo.getNickname(),
                                newMemberInfo.getProfileImageUrl()),
                        () -> { throw new UserCantUpdateException(); });
    }

    public Page<MemberDto> findAllMembers(final Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::from);
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(final String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

}
