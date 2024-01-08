package com.spring.batch.batchapplication.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FirstJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Before Job " + jobExecution.getJobInstance().getJobName());
		log.info("Job Params " + jobExecution.getJobParameters());
		log.info("Job Exec Context " + jobExecution.getExecutionContext());
		
		jobExecution.getExecutionContext().put("JobKey", "JobValue");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("After Job " + jobExecution.getJobInstance().getJobName());
		log.info("Job Params " + jobExecution.getJobParameters());
		log.info("Job Exec Context " + jobExecution.getExecutionContext());
//		log.info("Job : {} Exit Status : {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitCode());

		if(ExitStatus.FAILED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())){
			log.error("Job : {} Exit Status : {}", jobExecution.getJobInstance().getJobName(), jobExecution.getExitStatus().getExitCode());
//			log.error("job Failure Execution : {}", jobExecution.getAllFailureExceptions());
			//Can send email with exception details for this job
			jobExecution.getAllFailureExceptions()
					.stream().forEach(ex -> log.error("", ex));
//			log.error("Job Exit Description : {}", jobExecution.getExitStatus().getExitDescription());
		}

	}

}
