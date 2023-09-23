package kr.apartribebackend.auth;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.exception.EmailDuplicateException;
import kr.apartribebackend.member.exception.MalformedProfileImageLinkException;
import kr.apartribebackend.member.exception.NicknameDuplicateException;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.auth.dto.MemberJoinReq;
import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.exception.NotAuthenticatedEmailException;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
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

    @PostMapping("/join")
    public ResponseEntity<Void> memberJoin(@Valid @RequestBody final MemberJoinReq memberJoinReq) {
        if (!memberJoinReq.password().equals(memberJoinReq.passwordConfirm()))
            throw new PasswordNotEqualException();

        if (memberJoinReq.profileImageUrl() != null) {
            if (StringUtils.containsWhitespace(memberJoinReq.profileImageUrl()) ||
                    memberJoinReq.profileImageUrl().contains("..") || memberJoinReq.profileImageUrl().contains("\\"))
                throw new MalformedProfileImageLinkException();
        }

        if (memberRepository.existsByEmail(memberJoinReq.email()))
            throw new EmailDuplicateException();

        if (memberRepository.existsByNickname(memberJoinReq.nickname()))
            throw new NicknameDuplicateException();

        final EmailToken emailToken = emailTokenContextHolder
                .retrieveEmailTokenByEmail(memberJoinReq.email());
        if (emailToken == null || !emailToken.getValue().equals(memberJoinReq.code()))
            throw new NotAuthenticatedEmailException();

        final MemberDto memberDto = memberJoinReq.toDto();
        final Member member = memberDto.toEntity();
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
