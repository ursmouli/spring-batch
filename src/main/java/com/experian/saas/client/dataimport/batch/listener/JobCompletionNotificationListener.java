package com.experian.saas.client.dataimport.batch.listener;

import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private ConfigParameters configParameters;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("before job execution : {}", configParameters.getFileProcessDirPath());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LOGGER.info("after job execution : {}", configParameters.getFileProcessDirPath());
        // TODO send email on Job fail status
        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            LOGGER.error("job execution failed .. sending email to customer");
        }
    }
}
