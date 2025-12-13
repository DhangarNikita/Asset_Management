package com.asset.AssetManagement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobCompletionListener.class);
    public void beforeJob(JobExecution jobExecution) {
       logger.info("Job Started: "+jobExecution.getJobInstance().getJobName());
    }

    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("JOB FINISHED SUCCESSFULLY!");
        }
        else {
            logger.error("JOB FAILED! Status: {} ", jobExecution.getStatus());
        }
    }
}
