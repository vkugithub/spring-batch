package com.spring.batch.batchapplication.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class SecondBatchJobConfig {

    @Bean(name = "secondBatchJob")
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("secondBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step3(null, null))
                .next(step4(null, null))
                .build();
    }

    @Bean
    protected Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager ) {
        return new StepBuilder("step3", jobRepository)
                .tasklet(thirdTasklet(), transactionManager)
                .build();
    }

    private Tasklet thirdTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is third tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    protected Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager ) {
        return new StepBuilder("step4", jobRepository)
                .tasklet(forthTasklet(), transactionManager)
                .build();
    }

    private Tasklet forthTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is fourth tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
