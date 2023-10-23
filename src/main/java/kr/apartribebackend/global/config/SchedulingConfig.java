package kr.apartribebackend.global.config;

import kr.apartribebackend.token.email.config.EmailTokenContextHolder;
import kr.apartribebackend.token.email.domain.EmailToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Bean
    public UpdateRecruitStatusTask updateRecruitStatusTask(
            JobLauncher jobLauncher, Job updateRecruitStatusJob
    ) {
        return new UpdateRecruitStatusTask(jobLauncher, updateRecruitStatusJob);
    }

    @RequiredArgsConstructor
    static class ClearEmailTokenContextHolderTask {

        private final EmailTokenContextHolder emailTokenContextHolder;

        @Scheduled(cron = "0 0 */1 * * *", zone = "Asia/Seoul")
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

    @RequiredArgsConstructor
    static class UpdateRecruitStatusTask {

        private final JobLauncher jobLauncher;
        private final Job updateRecruitStatusJob;

        @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
        public void helloWorldJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
            JobParameters jobParameters = new JobParameters(
                    Collections.singletonMap(
                            "requestTime",
                            new JobParameter<>(System.currentTimeMillis(), Long.class)
                    )
            );
            jobLauncher.run(updateRecruitStatusJob, jobParameters);
        }

    }

}
