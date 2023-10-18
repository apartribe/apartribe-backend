package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.MemberJoinReq;
import kr.apartribebackend.member.dto.NicknameIsValidResponse;
import kr.apartribebackend.member.exception.EmailDuplicateException;
import kr.apartribebackend.member.exception.MalformedProfileImageLinkException;
import kr.apartribebackend.member.exception.NicknameDuplicateException;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.member.service.MemberService;
import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.dto.EmailTokenIsValidResponse;
import kr.apartribebackend.token.email.exception.NotAuthenticatedEmailException;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import kr.apartribebackend.token.email.service.EmailSenderService;
import kr.apartribebackend.token.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberJoinController {

    private final EmailTokenRepository emailTokenRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenContextHolder emailTokenContextHolder;
    private final MemberService memberService;
    private final EmailService emailService;
    private final EmailSenderService emailSenderService;

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

    @GetMapping("/member/check")
    public NicknameIsValidResponse checkDuplicateNickname(@RequestParam final String nickname) {
        if (memberService.existsByNickname(nickname))
            return new NicknameIsValidResponse(false);
        return new NicknameIsValidResponse(true);
    }

    @GetMapping("/email/send")
    public void sendEmailToken(@RequestParam final String email) {
        final String token = UUID.randomUUID().toString();
        log.info("token = {}", token);
        final EmailToken emailToken = EmailToken.builder()
                .value(token)
                .build();
        emailTokenContextHolder.appendEmailToken(email, emailToken);
        emailSenderService.send(email, token);
    }

    @GetMapping("/email/confirm")
    public EmailTokenIsValidResponse confirmEmailToken(
            @RequestParam final String email,
            @RequestParam final String token)
    {
        final EmailToken emailToken = emailTokenContextHolder.retrieveEmailTokenByEmail(email);
        if (emailToken == null)
            return new EmailTokenIsValidResponse(false);

        final String extractedTokenValue = emailToken.getValue();
        if (!extractedTokenValue.equals(token))
            return new EmailTokenIsValidResponse(false);

        if (emailService.existsByValue(token))
            return new EmailTokenIsValidResponse(false);

        if (emailToken.getExpiredAt().isBefore(LocalDateTime.now()))
            return new EmailTokenIsValidResponse(false);

        log.info("EmailToken isValid");
        emailToken.confirmEmailToken();

        return new EmailTokenIsValidResponse(true);
    }
}
