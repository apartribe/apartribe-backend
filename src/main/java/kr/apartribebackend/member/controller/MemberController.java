package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.MemberResponse;
import kr.apartribebackend.member.dto.MemberUpdateReq;
import kr.apartribebackend.member.dto.NicknameIsValidResponse;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/api/members")
    public APIResponse<PageResponse<MemberResponse>> findAllMembers(@PageableDefault final Pageable pageable) {
        final Page<MemberResponse> pageMembers = memberService.findAllMembers(pageable).map(MemberResponse::from);
        final PageResponse<MemberResponse> pageResponse = PageResponse.from(pageMembers);
        final APIResponse<PageResponse<MemberResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

    @PutMapping("/api/member/update")
    public void updateSingleMember(
            @RequestHeader(value = "EMAIL", required = false) final String email,                                   // TODO 나중에 토큰값에서 꺼내오는것으로 변경해야 한다.
            @RequestBody @Valid final MemberUpdateReq memberUpdateReq
    ) {
        if (!memberUpdateReq.password().equals(memberUpdateReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        final MemberDto memberDto = memberUpdateReq.toDto();
        memberService.updateSingleMember(memberDto, email);
    }

    @DeleteMapping("/api/member/delete")
    public void deleteSingleUser(@RequestHeader(value = "EMAIL", required = false) final String email) {            // TODO 나중에 토큰값에서 꺼내오는것으로 변경해야 한다.
        memberService.deleteSingleUser(email);
    }

    @GetMapping("/api/member/check")
    public NicknameIsValidResponse checkDuplicateNickname(@RequestParam final String nickname) {
        if (memberService.existsByNickname(nickname))
            return new NicknameIsValidResponse(false);
        return new NicknameIsValidResponse(true);
    }

}

