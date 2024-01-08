package com.spring.batch.batchapplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BatchJobLauncher {

    @Autowired
    private org.springframework.batch.core.launch.JobLauncher jobLauncher;

    @Autowired
    private Job firstBatchJob;

    @Autowired
    private Job secondBatchJob;

    @Autowired
    private Job chunkBatchJob;

    @Async
    public void launchJob(String jobName) {
        Job batchJob = getStringBatchJob(jobName);
        Map<String, JobParameter<?>> params = new HashMap();
        params.put("currentTime", new JobParameter(System.currentTimeMillis(), Long.class));
        var jobParameters = new JobParameters(params);
        try {
            log.info("Spring batch job : {} got started by rest api", jobName);
            var jobExceution = jobLauncher.run(batchJob, jobParameters);
            log.info("JobExecution Id : {}",jobExceution.getJobId());
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | JobRestartException ex) {
            log.error("Exception to start the batch job : {}", jobName, ex);
        }
    }

    private Job getStringBatchJob(String jobName){
        if("firstJob".equalsIgnoreCase(jobName))
            return firstBatchJob;
        else if("secondJob".equalsIgnoreCase(jobName))
            return secondBatchJob;
        else if("chunkJob".equalsIgnoreCase(jobName))
            return chunkBatchJob;
        else
            throw new RuntimeException("Invalid job name in request");
    }
}
