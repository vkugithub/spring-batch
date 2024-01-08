package com.spring.batch.batchapplication.config;

import com.spring.batch.batchapplication.listener.FirstJobListener;
import com.spring.batch.batchapplication.listener.FirstStepListener;
import com.spring.batch.batchapplication.listener.SecondStepListener;
import com.spring.batch.batchapplication.job.items.tasklets.Tasklet5;
import com.spring.batch.batchapplication.job.items.tasklets.Tasklet6;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class FirstBatchJobConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private FirstJobListener firstJobListener;

    @Autowired
    private FirstStepListener firstStepListener;

    @Autowired
    private SecondStepListener secondStepListener;

    @Autowired
    private JobExecutionDecider jobExecutionDecider;

    @Bean(name = "firstBatchJob")
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("firstBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
//                .start(step1())
//                .next(step2())
                .start(step1())
                    .on("COMPLETED").to(step2())
                .from(step2())
                    .on("COMPLETED").to(step5(null))
                .from(step2())
                    .on("*").to(step6(null))
                .end()
                .listener(firstJobListener)
                .build();
    }
//Job ->Step 1, Step2 ->
    @Bean
    protected Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(firstTasklet(), transactionManager)
                .listener(firstStepListener)
                .build();
    }

    private Tasklet firstTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is first tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    protected Step step2() {
        return new StepBuilder("step2", jobRepository)
//                .tasklet(firstTasklet(), transactionManager)
                .tasklet(secondTasklet(), transactionManager)
                .listener(secondStepListener)
                .build();
    }

    private Tasklet secondTasklet(){
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is second tasklet");
                var value= chunkContext.getStepContext().getJobExecutionContext().get("JobKey");
                log.info("Job context value : {}",value);
//                if(true)
//                    throw new RuntimeException("secondTasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    protected Step step5(Tasklet5 tasklet5 ) {
        return new StepBuilder("step5", jobRepository)
                .tasklet(tasklet5, transactionManager)
                .build();
    }

    @Bean
    protected Step step6(Tasklet6 tasklet6 ) {
        return new StepBuilder("step6", jobRepository)
                .tasklet(tasklet6, transactionManager)
                .build();
    }
}
