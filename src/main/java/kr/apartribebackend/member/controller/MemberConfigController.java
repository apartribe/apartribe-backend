package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.apart.dto.ApartmentDto;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.principal.AuthenticatedMember;
import kr.apartribebackend.member.service.MemberConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberConfigController {

    private final MemberConfigService memberConfigService;

    @GetMapping("/single")
    public APIResponse<SingleMemberResponse> findSingleMember(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        final SingleMemberResponse singleMemberResponse = memberConfigService
                .findMemberWithApartInfoByEmailAndMemberType(authenticatedMember.toDto());
        final APIResponse<SingleMemberResponse> apiResponse = APIResponse.SUCCESS(singleMemberResponse);
        return apiResponse;
    }

    @PutMapping("/update/image")
    public void updateSingleMemberProfileImage(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangeImageReq memberChangeImageReq
    ) {
        // TODO 비밀번호와 비밀번호 확인 일치는 Validator 로 빼자.
        memberConfigService.updateSingleMemberProfileImage(authenticatedMember, memberChangeImageReq);
    }

    @PutMapping("/update/password")
    public void updateSingleMemberPassword(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @Valid @RequestBody final MemberChangePasswordReq memberChangePasswordReq
    ) {
        // TODO 비밀번호와 비밀번호 확인 일치는 Validator 로 빼자.
        if (!memberChangePasswordReq.newPassword().equals(memberChangePasswordReq.passwordConfirm())) {
            throw new PasswordNotEqualException();
        }
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
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final ApartmentDto apartmentDto = authenticatedMember.getApartmentDto();
        final Page<MemberCommentRes> memberCommentRes = memberConfigService.fetchCommentsForMember(memberDto, apartmentDto, pageable);
        final PageResponse<MemberCommentRes> pageResponse = PageResponse.from(memberCommentRes);
        final APIResponse<PageResponse<MemberCommentRes>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @GetMapping("/article")
    public APIResponse<PageResponse<MemberBoardResponse>> fetchArticlesForMember(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final MemberDto memberDto = authenticatedMember.toDto();
        final ApartmentDto apartmentDto = authenticatedMember.getApartmentDto();
        final Page<MemberBoardResponse> memberArticleRes = memberConfigService.fetchArticlesForMember(memberDto, apartmentDto, pageable);
        final PageResponse<MemberBoardResponse> pageResponse = PageResponse.from(memberArticleRes);
        final APIResponse<PageResponse<MemberBoardResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @DeleteMapping("/delete")
    public void deleteSingleUser(
            @AuthenticationPrincipal final AuthenticatedMember authenticatedMember
    ) {
        memberConfigService.deleteSingleUser(authenticatedMember.toDto());
    }

}
