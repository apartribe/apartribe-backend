package kr.apartribebackend.member.controller;

import jakarta.validation.Valid;
import kr.apartribebackend.global.exception.PasswordNotEqualException;
import kr.apartribebackend.member.domain.Member;
import kr.apartribebackend.member.domain.MemberType;
import kr.apartribebackend.member.domain.agreements.Agreements;
import kr.apartribebackend.member.domain.forgot.Forgot;
import kr.apartribebackend.member.dto.MemberDto;
import kr.apartribebackend.member.dto.MemberJoinReq;
import kr.apartribebackend.member.dto.NicknameIsValidResponse;
import kr.apartribebackend.member.dto.agreements.AgreementsDto;
import kr.apartribebackend.member.dto.forgot.ForgotReq;
import kr.apartribebackend.member.dto.forgot.ResetPasswordReq;
import kr.apartribebackend.member.exception.*;
import kr.apartribebackend.member.repository.MemberRepository;
import kr.apartribebackend.member.repository.agreements.AgreementsRepository;
import kr.apartribebackend.member.repository.forgot.ForgotRepository;
import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.dto.EmailTokenIsValidResponse;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import kr.apartribebackend.token.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static kr.apartribebackend.member.domain.MemberType.*;
import static org.springframework.http.HttpStatus.CREATED;

// TODO 생각보다 코드의 양이 많음. 따라서 Service 계층으로 분리시킬 필요가 있음.
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class MemberJoinController {

    @Value("${application.frontend.redirect-uri}") private String frontendUri;
    private final EmailTokenRepository emailTokenRepository;
    private final MemberRepository memberRepository;
    private final AgreementsRepository agreementsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenContextHolder emailTokenContextHolder;
    private final EmailSenderService emailSenderService;
    private final ForgotRepository forgotRepository;

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

    @PostMapping("/forgot/password")
    public void forgotPassword(@Valid @RequestBody final ForgotReq forgotReq) {
        final Member findedMember = memberRepository.findByEmailAndNameAndMemberType(forgotReq.email(), forgotReq.name(), GENERAL)
                .orElseThrow(UserInfoNotFoundException::new);
        final String identifier = UUID.randomUUID().toString();
        forgotRepository.findForgotByMemberId(findedMember.getId())
                .ifPresentOrElse(
                        forgot -> forgotRepository.updateForgotLinkByMemberId(
                                LocalDateTime.now().plusMinutes(3), identifier, forgot.getId()
                        ),
                        () -> forgotRepository.save(
                                Forgot.builder().member(findedMember)
                                        .identifier(identifier)
                                        .email(findedMember.getEmail())
                                        .name(findedMember.getName())
                                        .build()
                        )
                );
        final String uriString = UriComponentsBuilder
                .fromUriString(frontendUri)
                .path("find/pw/reset")
                .queryParam("identifier", identifier)
                .toUriString();

        emailSenderService.send(findedMember.getEmail(), uriString, "비밀번호 재설정 링크 안내");
    }

    @PostMapping("/forgot/password/confirm")
    public void forgotPasswordConfirm(
            @RequestParam final String identifier,
            @Valid @RequestBody final ResetPasswordReq resetPasswordReq
    ) {
        if (!resetPasswordReq.password().equals(resetPasswordReq.passwordConfirm())) {
            throw new PasswordNotEqualException();
        }
        final Forgot findedForgot = forgotRepository.findForgotWithMemberByIdentifier(identifier).orElse(null);
        if (findedForgot == null) {
            throw new InvalidMemberInfoResetPasswordException();
        }
        if (findedForgot.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredResetPasswordLinkException();
        }
        final Member forgotMember = findedForgot.getMember();
        if (
                !findedForgot.getName().equals(forgotMember.getName()) ||
                !findedForgot.getEmail().equals(forgotMember.getEmail())
        ) {
            throw new InvalidPasswordResetIdentifierException();
        }
        memberRepository.changePasswordByMemberId(
                findedForgot.getMember().getId(), passwordEncoder.encode(resetPasswordReq.password())
        );
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
        if (memberRepository.existsByEmailAndMemberType(memberJoinReq.email(), GENERAL)) {
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
