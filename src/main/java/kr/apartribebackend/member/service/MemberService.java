package kr.apartribebackend.member.service;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.exception.UserCantDeleteException;
import kr.apartribebackend.member.exception.UserCantUpdateException;
import kr.apartribebackend.member.exception.UserNotFoundException;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberDto findSingleMember(final String email) {
        return memberRepository.findByEmail(email)
                .map(MemberDto::from)
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
                                newMemberInfo.getNickname()),
                        () -> { throw new UserCantUpdateException(); });
    }

    public void deleteSingleUser(final String email) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        memberRepository::delete, () -> { throw new UserCantDeleteException(); }
                );
    }

}
