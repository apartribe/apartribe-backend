package kr.apartribebackend.member.controller;

import kr.apartribebackend.global.dto.APIResponse;
import kr.apartribebackend.global.dto.PageResponse;
import kr.apartribebackend.member.dto.*;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/member")
    public APIResponse<PageResponse<MemberResponse>> findAllMembers(@PageableDefault final Pageable pageable) {
        final Page<MemberResponse> pageMembers = memberService.findAllMembers(pageable).map(MemberResponse::from);
        final PageResponse<MemberResponse> pageResponse = PageResponse.from(pageMembers);
        final APIResponse<PageResponse<MemberResponse>> apiResponse = APIResponse.SUCCESS(pageResponse);
        return apiResponse;
    }

}

