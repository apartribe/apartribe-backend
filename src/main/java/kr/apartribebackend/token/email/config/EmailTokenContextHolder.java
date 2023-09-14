package kr.apartribebackend.token.email.config;

import kr.apartribebackend.token.email.domain.EmailToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public final class EmailTokenContextHolder {

    private final Map<String, EmailToken> emailTokenContextHolder = new ConcurrentHashMap<>();

    private EmailTokenContextHolder() {}

    public void appendEmailToken(String email, EmailToken emailToken) {
        emailTokenContextHolder.put(email, emailToken);
    }

    public void removeEmailTokenByEmail(String email) {
        emailTokenContextHolder.remove(email);
    }

    public EmailToken retrieveEmailTokenByEmail(String email) {
        return emailTokenContextHolder.get(email);
    }

}
