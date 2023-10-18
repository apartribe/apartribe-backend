package kr.apartribebackend.global.config;

import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@EnableScheduling
@Configuration
public class SchedulingConfig {

    @Bean
    public ClearEmailTokenContextHolderTask clearEmailTokenContextHolderTask(
            EmailTokenContextHolder emailTokenContextHolder
    ) {
        return new ClearEmailTokenContextHolderTask(emailTokenContextHolder);
    }

    @RequiredArgsConstructor
    static class ClearEmailTokenContextHolderTask {

        private final EmailTokenContextHolder emailTokenContextHolder;

        @Scheduled(cron = "0 0 */1 * * *")
        public void clearEmailContext() {
            log.info("Clearing Remaining ExpiredEmailTokens 1 Hour Cycle");
            detectAndKillLeftEmailTokens();
        }

        public void detectAndKillLeftEmailTokens() {
            final Map<String, EmailToken> emailContextHolder = emailTokenContextHolder.getEmailTokenContextHolder();
            for (String emailInfo : emailContextHolder.keySet()) {
                if (emailContextHolder.get(emailInfo).getExpiredAt().isBefore(LocalDateTime.now())) {
                    emailContextHolder.remove(emailInfo);
                }
            }
        }
    }

}
