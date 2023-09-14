package kr.apartribebackend.token.email.controller;


import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.dto.EmailTokenIsValidResponse;
import kr.apartribebackend.token.email.service.EmailSenderService;
import kr.apartribebackend.token.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    private final EmailSenderService emailSenderService;

    private final EmailTokenContextHolder emailTokenContextHolder;

    @GetMapping("/api/email/send")
    public void sendEmailToken(@RequestParam final String email) {
        final String token = UUID.randomUUID().toString();
        log.info("token = {}", token);
        EmailToken emailToken = EmailToken.builder()
                .value(token)
                .build();
        emailTokenContextHolder.appendEmailToken(email, emailToken);
        emailSenderService.send(email, "test", token);
    }

    @GetMapping("/api/email/confirm")
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

        emailService.save(emailToken);
        return new EmailTokenIsValidResponse(true);
    }

//    @GetMapping("/api/email/send")
//    public void sendEmailToken(@RequestParam final String email) {
//        final String token = UUID.randomUUID().toString();
//        log.info("token = {}", token);
//        emailTokenContextHolder.appendEmailToken(email, token);
//        emailSenderService.send(email, "test", token);
//    }

//    @GetMapping("/api/email/confirm")
//    public EmailTokenIsValidResponse confirmEmailToken(
//            @RequestParam final String email,
//            @RequestParam final String token)
//    {
////        final String extractedToken = emailTokenContextHolder.retrieveEmailTokenByEmail(email)
////                .orElseThrow(EmailTokenNotExistsException::new);
//        final String extractedToken = emailTokenContextHolder.retrieveEmailTokenByEmail(email);
//        if (extractedToken == null) {
//            return new EmailTokenIsValidResponse(false);
//
//        }
//        if (!extractedToken.equals(token))
//            return new EmailTokenIsValidResponse(false);
////            throw new EmailTokenInvalidException();
//
//        if (emailService.existsByValue(token))
//            return new EmailTokenIsValidResponse(false);
////            throw new EmailTokenDuplicateException();
//
//        log.info("EmailToken isValid");
//        final EmailToken emailToken = EmailToken.builder()
//                .value(token)
//                .build();
//        emailToken.confirmEmailToken();
//
//        emailService.save(emailToken);
////        emailTokenContextHolder.removeEmailTokenByEmail(email);
//        return new EmailTokenIsValidResponse(true);
//    }
}
