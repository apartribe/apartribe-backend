package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.agreements.Agreements;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.MemberJoinReq;
import kr.apartribebackend.member.dto.NicknameIsValidResponse;
import kr.apartribebackend.member.dto.agreements.AgreementsDto;
import kr.apartribebackend.member.exception.*;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.member.repository.agreements.AgreementsRepository;
import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.dto.EmailTokenIsValidResponse;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import kr.apartribebackend.token.email.service.EmailSenderService;
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
    private final AgreementsRepository agreementsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenContextHolder emailTokenContextHolder;
    private final EmailSenderService emailSenderService;

    @PostMapping("/join")
    public ResponseEntity<Void> memberJoin(@Valid @RequestBody final MemberJoinReq memberJoinReq) {
        validateMemberRequest(memberJoinReq);
        final MemberDto memberDto = memberJoinReq.toMemberDto();
        final AgreementsDto agreementsDto = memberJoinReq.toAgreementsDto();

        final Member member = memberDto.toEntity();
        final Agreements agreements = agreementsDto.toEntity(member);

        final EmailToken emailTokenByEmail = emailTokenRepository.findEmailTokenByEmail(memberJoinReq.email());
        if (emailTokenByEmail == null) {
            throw new NotExistsEmailTokenForEmailException();
        }
        if (emailTokenByEmail.getConfirmedAt() == null) {
            throw new NotExistsEmailTokenForEmailException();
        }
        if (!emailTokenByEmail.getValue().equals(memberJoinReq.code())) {
            throw new InvalidEmailTokenForEmailException();
        }

        member.changePassword(passwordEncoder.encode(memberJoinReq.password()));
        emailTokenByEmail.changeMember(member);
        memberRepository.save(member);
        agreementsRepository.save(agreements);

        return ResponseEntity
                .status(CREATED)
                .build();
    }

    @GetMapping("/member/check")
    public NicknameIsValidResponse checkDuplicateNickname(@RequestParam final String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return new NicknameIsValidResponse(false);
        }
        return new NicknameIsValidResponse(true);
    }

    @GetMapping("/email/send")
    public void sendEmailToken(@RequestParam final String email) {
        final EmailToken emailTokenByEmail = emailTokenRepository.findEmailTokenByEmail(email);
        final String token = UUID.randomUUID().toString();
        if (emailTokenByEmail == null) { // email_token 이 없으면 인증조차 하지 않은 사용자. (발송)
            log.info("Issue EmailToken = {}", token);
            final EmailToken emailToken = EmailToken.builder().value(token).email(email).build();
            emailTokenContextHolder.appendEmailToken(email, emailToken);
            emailSenderService.send(email, token);
            return;
        }
        if (emailTokenByEmail.getMember() == null) { // email_token 이 있는데, 인증은되었는데, 회원가입을 하지 않은 사용자. (재발송)
            log.info("ReIssue EmailToken = {}", token);
            emailTokenByEmail.updateTokenValue(token);
            emailTokenRepository.updateEmailTokenValue(token, LocalDateTime.now(), emailTokenByEmail.getId());
            emailTokenContextHolder.appendEmailToken(email, emailTokenByEmail);
            emailSenderService.send(email, token);
            return;
        }
        throw new CantSendMailCauseAlreadyExistsTokenException();
    }

    @GetMapping("/email/confirm")
    public EmailTokenIsValidResponse confirmEmailToken(
            @RequestParam final String email,
            @RequestParam final String token
    ) {
        final EmailToken emailToken = emailTokenContextHolder.retrieveEmailTokenByEmail(email);
        if (!isReadyToConfirmEmailToken(token, emailToken)) {
            return new EmailTokenIsValidResponse(false);
        }
        if (emailTokenRepository.existsByEmailAndMemberNotNull(email)) {
            return new EmailTokenIsValidResponse(false);
        }
        log.info("EmailToken isValid");
        emailToken.confirmEmailToken(emailToken.getEmail());
        emailTokenRepository.save(emailToken);
        emailTokenContextHolder.removeEmailTokenByEmail(email);
        return new EmailTokenIsValidResponse(true);
    }

    private void validateMemberRequest(final MemberJoinReq memberJoinReq) {
        if (!memberJoinReq.password().equals(memberJoinReq.passwordConfirm())) {
            throw new PasswordNotEqualException();
        }
        if (memberJoinReq.profileImageUrl() != null) {
            if (
                    StringUtils.containsWhitespace(memberJoinReq.profileImageUrl()) ||
                    memberJoinReq.profileImageUrl().contains("..") ||
                    memberJoinReq.profileImageUrl().contains("\\")
            ) {
                throw new MalformedProfileImageLinkException();
            }
        }
        if (memberRepository.existsByEmail(memberJoinReq.email())) {
            throw new EmailDuplicateException();
        }
        if (memberRepository.existsByNickname(memberJoinReq.nickname())) {
            throw new NicknameDuplicateException();
        }
    }

    private boolean isReadyToConfirmEmailToken(final String token, final EmailToken emailToken) {
        if (emailToken == null) {
            return false;
        }
        final String extractedTokenValue = emailToken.getValue();
        if (!extractedTokenValue.equals(token)) {
            return false;
        }
        if (emailToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}
