package com.spring.batch.batchapplication.config;

import com.spring.batch.batchapplication.listener.FirstJobListener;
import com.spring.batch.batchapplication.listener.FirstStepListener;
import com.spring.batch.batchapplication.listener.SkipListener;
import com.spring.batch.batchapplication.model.StudentCsv;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import com.spring.batch.batchapplication.job.items.reader.FirstItemReader;
import com.spring.batch.batchapplication.job.items.processor.FirstItemProcessor;
import com.spring.batch.batchapplication.job.items.writer.FirstItemWriter;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@Slf4j
public class ChunkBatchJobConfig {

    @Autowired
    private FirstItemReader firstItemReader;

    @Autowired
    private FirstItemProcessor firstItemProcessor;

    @Autowired
    private FirstItemWriter firstItemWriter;

    @Autowired
    private SkipListener skipListener;

    @Autowired
    private FirstStepListener firstStepListener;

    @Bean(name = "chunkBatchJob")
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("chunkBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processCSVFileStep(null, null))
//                .next(step4(null, null))
                .build();
    }

    @Bean("chunkStep")
    public Step processCSVFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("chunkStep", jobRepository)
                .<Integer, Long>chunk(3, transactionManager)
                .<Integer, Long>reader(firstItemReader)
                .<Integer, Long>processor(firstItemProcessor)
                .writer(firstItemWriter)
//                .faultTolerant()
//                .skip(Exception.class)
//                .skipLimit(10)
////                .skipPolicy(new AlwaysSkipItemSkipPolicy())
//                .retry(RuntimeException.class)
//                .retryLimit(2)
//                .listener(skipListener)
////                .listener(skipListenerImpl)
//                .listener(firstStepListener)
                .build();
    }

}
