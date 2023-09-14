package kr.apartribebackend.token.email.service;

import kr.apartribebackend.token.email.domain.EmailToken;
import kr.apartribebackend.token.email.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailTokenRepository emailTokenRepository;

    public boolean existsByValue(String token) {
        return emailTokenRepository.existsByValue(token);
    }

    @Transactional
    public void save(EmailToken emailToken) {
        emailTokenRepository.save(emailToken);
    }
}
