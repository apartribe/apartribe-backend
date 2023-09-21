package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController @RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/member/{email}")
    public APIResponse<MemberResponse> findSingleMember(@PathVariable final String email) {
        final MemberDto singleMember = memberService.findSingleMember(email);
        final MemberResponse memberResponse = MemberResponse.from(singleMember);
        final APIResponse<MemberResponse> apiResponse = APIResponse.SUCCESS(memberResponse);
        return apiResponse;
    }

    @GetMapping("/api/member")
    public APIResponse<PageResponse<MemberResponse>> findAllMembers(@PageableDefault final Pageable pageable) {
        final Page<MemberResponse> pageMembers = memberService.findAllMembers(pageable).map(MemberResponse::from);
        final PageResponse<MemberResponse> pageResponse = PageResponse.from(pageMembers);
        final APIResponse<PageResponse<MemberResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    // TODO 아직 수정 전이며, 보류
    @PutMapping("/api/member/update")
    public void updateSingleMember(
            @RequestHeader(value = "EMAIL", required = false) final String email,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @RequestBody @Valid final MemberUpdateReq memberUpdateReq
    ) {
        if (!memberUpdateReq.password().equals(memberUpdateReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        final MemberDto memberDto = memberUpdateReq.toDto();
        memberService.updateSingleMember(memberDto, email);
    }

    @PutMapping("/api/member/update/password")
    public void updateSingleMemberPassword(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangePasswordReq memberChangePasswordReq
    ) {
        if (!memberChangePasswordReq.newPassword().equals(memberChangePasswordReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        memberService.updateSingleMemberPassword(authenticatedMember, memberChangePasswordReq);
    }

    @PutMapping("/api/member/update/nickname")
    public void updateSingleMemberNickname(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangeNicknameReq memberChangeNicknameReq
    ) {
        memberService.updateSingleMemberNickname(authenticatedMember, memberChangeNicknameReq.nickname());
    }

    // TODO 아직 수정 전이며, 보류
    @DeleteMapping("/api/member/delete")
    public void deleteSingleUser(
            @RequestHeader(value = "EMAIL", required = false) final String email,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        memberService.deleteSingleUser(email);
    }

    @GetMapping("/api/auth/member/check")
    public NicknameIsValidResponse checkDuplicateNickname(@RequestParam final String nickname) {
        if (memberService.existsByNickname(nickname))
            return new NicknameIsValidResponse(false);
        return new NicknameIsValidResponse(true);
    }

}

