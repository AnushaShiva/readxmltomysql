package com.example.demo.controller;

import org.joda.time.DateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BatchController {

	
	@Autowired
	private JobLauncher jobLauncher;
	
	
	@Autowired
	@Qualifier("job")
	private Job job;
	
	
	@Bean
	@GetMapping(path = "/job")
	public String userJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters jobParameters = new JobParametersBuilder()
				.toJobParameters();
        log.info("Job is Starting with this name : {}",job.getName());
		  return"Job ended with status : "+jobLauncher.run(job , jobParameters).getStatus().getBatchStatus()+  " at "+DateTime.now();
		
	}
	
	
	
	
	
}
