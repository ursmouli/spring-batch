package com.experian.saas.client.dataimport.batch.config;

import com.experian.saas.client.dataimport.batch.scheduler.BatchScheduler;
import com.experian.saas.client.dataimport.batch.tasklet.FileRenameMovingTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class FileRenameAndMoveConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileRenameAndMoveConfig.class);

    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FileRenameMovingTasklet fileRenameMovingTasklet;

    @Scheduled(fixedRate = 5000)
    public void perform() {
        JobParameters parameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution renameAndMoveFileJob = null;
        try {
            renameAndMoveFileJob = simpleJobLauncher.run(renameAndMoveFileJob(), parameters);
            LOGGER.info("RenameAndMoveFile Job finished with status : {}", renameAndMoveFileJob.getStatus());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    // Files renaming and moving tasklet
    @Bean
    public Step renameAndMoveFileStep() {
        return stepBuilderFactory.get("renameAndMoveFileStep")
                .tasklet(fileRenameMovingTasklet)
                .build();
    }
    // End - Files renaming and moving tasklet

    public Job renameAndMoveFileJob() {
        return jobBuilderFactory.get("renameAndMoveFileJob")
                .flow(renameAndMoveFileStep())
                .end()
                .build();
    }

}
