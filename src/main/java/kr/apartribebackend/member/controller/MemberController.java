package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.MemberResponse;
import kr.apartribebackend.member.dto.MemberUpdateReq;
import kr.apartribebackend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController @RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/member/{email}")
    public MemberResponse findSingleMember(@PathVariable final String email) {
        final MemberDto singleMember = memberService.findSingleMember(email);
        return MemberResponse.from(singleMember);
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

}

