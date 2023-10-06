package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.service.MemberConfigService;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberConfigController {

    private final MemberService memberService;
    private final MemberConfigService memberConfigService;

    @GetMapping("/{email}")
    public APIResponse<MemberResponse> findSingleMember(@PathVariable final String email) {
        final MemberDto singleMember = memberService.findSingleMember(email);
        final MemberResponse memberResponse = MemberResponse.from(singleMember);
        final APIResponse<MemberResponse> apiResponse = APIResponse.SUCCESS(memberResponse);
        return apiResponse;
    }

    @PutMapping("/update/password")
    public void updateSingleMemberPassword(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangePasswordReq memberChangePasswordReq
    ) {
        if (!memberChangePasswordReq.newPassword().equals(memberChangePasswordReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        memberConfigService.updateSingleMemberPassword(authenticatedMember, memberChangePasswordReq);
    }

    @PutMapping("/update/nickname")
    public void updateSingleMemberNickname(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangeNicknameReq memberChangeNicknameReq
    ) {
        memberConfigService.updateSingleMemberNickname(authenticatedMember, memberChangeNicknameReq.nickname());
    }

    @GetMapping("/comment")
    public APIResponse<PageResponse<MemberCommentRes>> fetchCommentsForMember(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PageableDefault final Pageable pageable
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final Page<MemberCommentRes> memberCommentRes = memberConfigService.fetchCommentsForMember(memberDto, pageable);
        final PageResponse<MemberCommentRes> pageResponse = PageResponse.from(memberCommentRes);
        APIResponse<PageResponse<MemberCommentRes>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    // TODO 시간이 남으면 댓글 CountQuery 까지 해보자.
    @GetMapping("/article")
    public APIResponse<PageResponse<MemberArticleRes>> fetchArticlesForMember(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PageableDefault final Pageable pageable
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final Page<MemberArticleRes> memberArticleRes = memberConfigService.fetchArticlesForMember(memberDto, pageable);
        final PageResponse<MemberArticleRes> pageResponse = PageResponse.from(memberArticleRes);
        final APIResponse<PageResponse<MemberArticleRes>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    // TODO 개발 전
    @DeleteMapping("/delete")
    public void deleteSingleUser(
            @RequestHeader(value = "EMAIL", required = false) final String email,
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        memberConfigService.deleteSingleUser(email);
    }

}