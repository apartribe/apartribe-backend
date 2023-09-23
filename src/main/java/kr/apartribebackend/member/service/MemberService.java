package kr.apartribebackend.member.service;

import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberChangePasswordReq;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.exception.*;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

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
                                newMemberInfo.getNickname(),
                                newMemberInfo.getProfileImageUrl()),
                        () -> { throw new UserCantUpdateException(); });
    }

    public void deleteSingleUser(final String email) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        memberRepository::delete, () -> { throw new UserCantDeleteException(); }
                );
    }

    public Page<MemberDto> findAllMembers(final Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::from);
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(final String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void updateSingleMemberPassword(final AuthenticatedMember authenticatedMember,
                                           final MemberChangePasswordReq memberChangePasswordReq) {
        if (!passwordEncoder.matches(memberChangePasswordReq.currentPassword(), authenticatedMember.getPassword()))
            throw new UserCantUpdatePasswordException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.changePassword(memberChangePasswordReq.newPassword());
    }

    public void updateSingleMemberNickname(final AuthenticatedMember authenticatedMember, final String nickname) {
        if (existsByNickname(nickname))
            throw new UserCantUpdateNicknameException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.updateNickname(nickname);
    }

}
