package kr.apartribebackend.auth;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.global.service.JwtService;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.exception.EmailDuplicateException;
import kr.apartribebackend.member.exception.NicknameDuplicateException;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.auth.dto.MemberJoinReq;
import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.exception.NotAuthenticatedEmailException;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import kr.apartribebackend.token.refresh.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailTokenRepository emailTokenRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailTokenContextHolder emailTokenContextHolder;

    private final JwtService jwtService;

    @PostMapping("/join")
    public ResponseEntity<Void> memberJoin(@Valid @RequestBody final MemberJoinReq memberJoinReq) {
        if (!memberJoinReq.password().equals(memberJoinReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        if (memberRepository.existsByEmail(memberJoinReq.email()))
            throw new EmailDuplicateException();

        if (memberRepository.existsByNickname(memberJoinReq.nickname()))
            throw new NicknameDuplicateException();

        final EmailToken emailToken = emailTokenContextHolder
                .retrieveEmailTokenByEmail(memberJoinReq.email());
        if (emailToken == null || !emailToken.getValue().equals(memberJoinReq.code()))
            throw new NotAuthenticatedEmailException();

        // TODO 이메일토큰을 인증하지않아도 회원가입이되는 것을 수정해야함.
        final MemberDto memberDto = memberJoinReq.toDto();
        String refToken = jwtService.generateRefreshToken(memberDto.getNickname());
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refToken)
                .build();

        final Member member = memberDto.toEntity(refreshToken);
        member.changePassword(passwordEncoder.encode(memberJoinReq.password()));
        emailToken.changeMember(member);
        memberRepository.save(member);
        emailTokenRepository.save(emailToken);

        emailTokenContextHolder.removeEmailTokenByEmail(member.getEmail());

        return ResponseEntity
                .status(CREATED)
                .build();
    }

//    @PostMapping("/forgot/id")
//    public void forgotId(@Valid @RequestBody final ForgotIdReq forgotIdReq) {
//        memberRepository.findByEmailAndName(forgotIdReq.email(), forgotIdReq.name())
//                .ifPresentOrElse(member -> emailSenderService.send(
//                        member.getEmail(),
//                        "계정 정보 확인을 위한 아이디 안내 -APARTRIBE-",
//                        member.getMeasdf dasdf);
//    }
//
//    @PostMapping("/forgot/password")
//    public void forgotPassword() {
//
//    }

}
