package kr.apartribebackend.global.config;

import kr.apartribebackend.article.domain.RecruitStatus;
import kr.apartribebackend.article.domain.Together;
import kr.apartribebackend.article.repository.together.TogetherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

// TODO 왜 애플리케이션 실행 시, 자동으로 Batch Table 들이 생기지 않는지는 아직 모르겠다.
//  임시방편으로 org.springframework.batch.core 에서 각 db 에 맞는 스크립트 파일 찾아서
//  sql 콘솔에서 쿼리문을 실행하여 Batch Table 을 만들어줄 수 있다. 예를 들면 h2 는 schema-h2.sql 파일이다.
@Slf4j
@RequiredArgsConstructor
//@EnableBatchProcessing
@Configuration
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TogetherRepository togetherRepository;

    @Bean
    public Job updateRecruitStatusJob(Step updateRecruitStatusStep) {
        return new JobBuilder("updateRecruitStatusJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(updateRecruitStatusStep)
                .listener(new JobLoggerListener())
                .build();
    }

    @JobScope
    @Bean
    public Step updateRecruitStatusStep(
            ItemReader<Together> togetherItemReader,
            ItemProcessor<Together, Together> togetherItemProcessor,
            ItemWriter<Together> togetherItemWriter
    ) {
        return new StepBuilder("updateRecruitStatusStep", jobRepository)
                .<Together, Together>chunk(300, platformTransactionManager)
                .reader(togetherItemReader)
                .processor(togetherItemProcessor)
                .writer(togetherItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Together> togetherItemReader() {
        return new RepositoryItemReaderBuilder<Together>()
                .name("togetherItemReader")
                .repository(togetherRepository)
                .methodName("findAll")
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .pageSize(300)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Together, Together> togetherItemProcessor() {
        return together -> {
            Constructor<Together> declaredConstructor = Together.class.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            return declaredConstructor.newInstance();
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Together> togetherItemWriter() {
        return chunk -> togetherRepository.updateRecruitStatusByRecruitTo(RecruitStatus.END, LocalDate.now());
    }

    @Slf4j
    static class JobLoggerListener implements JobExecutionListener {

        @Override
        public void beforeJob(JobExecution jobExecution) {
            log.info("{} Job is Running", jobExecution.getJobInstance().getJobName());
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            log.info("{} Job is Done. (Status : {})",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());

            if (jobExecution.getStatus() == BatchStatus.FAILED) {
                log.info("Job is Failed");
            }
        }
    }

}
