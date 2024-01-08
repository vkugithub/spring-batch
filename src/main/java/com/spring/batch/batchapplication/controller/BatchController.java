package com.spring.batch.batchapplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RequestMapping("/api/batch")
@RestController
public class BatchController {

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private BatchJobLauncher batchJobLauncher;

    @RequestMapping(method=GET, value = "/start/job/{jobName}")
    public String startSecondJob(@PathVariable String jobName) {
         batchJobLauncher.launchJob(jobName);
        return "Job Started...";
    }

    @GetMapping("/stop/job/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Job Stopped...";
    }

}
