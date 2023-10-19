package kr.apartribebackend.member.service;

import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.exception.UserCantDeleteException;
import kr.apartribebackend.member.exception.UserCantUpdateNicknameException;
import kr.apartribebackend.member.exception.UserCantUpdatePasswordException;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.repository.MemberConfigRepository;
import kr.apartribebackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class MemberConfigService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberConfigRepository memberConfigRepository;

    public void updateSingleMemberNickname(final AuthenticatedMember authenticatedMember, final String nickname) {
        if (memberRepository.existsByNickname(nickname))
            throw new UserCantUpdateNicknameException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.updateNickname(nickname);
    }

    public void updateSingleMemberPassword(final AuthenticatedMember authenticatedMember,
                                           final MemberChangePasswordReq memberChangePasswordReq) {
        if (!passwordEncoder.matches(memberChangePasswordReq.currentPassword(), authenticatedMember.getPassword()))
            throw new UserCantUpdatePasswordException();
        final Member member = authenticatedMember.getOriginalEntity();
        member.changePassword(memberChangePasswordReq.newPassword());
    }

    public void deleteSingleUser(final String email) {
        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        memberRepository::delete, () -> { throw new UserCantDeleteException(); }
                );
    }

    @Transactional(readOnly = true)
    public Page<MemberCommentRes> fetchCommentsForMember(final MemberDto memberDto,
                                                         final Pageable pageable) {
        return memberConfigRepository.findCommentsForMember(memberDto.toEntity(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<MemberBoardResponse> fetchArticlesForMember(final MemberDto memberDto,
                                                            final ApartmentDto apartmentDto,
                                                            final Pageable pageable) {
        return memberConfigRepository.findArticlesForMember(
                memberDto.toEntity(),
                apartmentDto.toEntity(),
                pageable
        );
    }

}
