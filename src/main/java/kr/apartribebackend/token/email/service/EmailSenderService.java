package kr.apartribebackend.token.email.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;


@Service @RequiredArgsConstructor @Slf4j
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${application.mail.subject}")
    private String subject;

    public void send(String to, String body) {
        send(to, body, null);
    }

    @Async
    public void send(String to, String body, String subject) {
        try {
            final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, String.valueOf(StandardCharsets.UTF_8)
            );
            mimeMessageHelper.setText(buildEmail(body), true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject != null ? subject : this.subject);
            mimeMessageHelper.setFrom(email);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email : {}", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    private String buildEmail(String code) {
        return "<table style=\"width: 100%; max-width: 600px; margin: 0 auto;\">\n" +
                "    <tr>\n" +
                "        <td style=\"text-align: center; background-color: #f5f5f5; padding-bottom: 50px; border-radius: 10px;\">\n" +
                "            <img src=\"https://res.cloudinary.com/dh6tdcdyj/image/upload/v1685938086/logoBg_lmdhiz.png\" alt=\"로고 이미지\" style=\" border-radius: 10px; margin-bottom:30px\">\n" +
                "            <p style=\"font-size:20px; line-height:50px; color: #148888; font-weight:900;\">다음 인증코드를 사이트의 입력란에 입력해주세요.</p>\n" +
                "            <p>인증코드 : <span style=\"font-size:20px; line-height:50px; color: #148888; font-weight:900;\">" + code + "</span></p>\n" +
                "            <p style=\"font-size:20px; line-height:50px; color: #148888; font-weight:900;\">본 인증 코드는 발급 기준 3분뒤 만기됩니다.</p>\n" +
                "            <br/>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n";
    }

}
