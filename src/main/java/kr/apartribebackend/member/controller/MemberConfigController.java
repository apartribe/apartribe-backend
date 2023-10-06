package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.comment.domain.Comment;
import kr.apartribebackend.comment.dto.CommentDto;
import kr.apartribebackend.comment.service.CommentService;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberConfigController {

    private final MemberService memberService;

    @GetMapping("/api/member/{email}")
    public APIResponse<MemberResponse> findSingleMember(@PathVariable final String email) {
        final MemberDto singleMember = memberService.findSingleMember(email);
        final MemberResponse memberResponse = MemberResponse.from(singleMember);
        final APIResponse<MemberResponse> apiResponse = APIResponse.SUCCESS(memberResponse);
        return apiResponse;
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

    // TODO 아직 개발 전
    @DeleteMapping("/api/member/delete")
    public void deleteSingleUser(
            @RequestHeader(value = "EMAIL", required = false) final String email,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        memberService.deleteSingleUser(email);
    }

}
