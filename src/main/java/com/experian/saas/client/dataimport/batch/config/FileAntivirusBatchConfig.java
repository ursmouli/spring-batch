package com.experian.saas.client.dataimport.batch.config;

import com.experian.saas.client.dataimport.batch.scheduler.BatchScheduler;
import com.experian.saas.client.dataimport.batch.tasklet.AvProcessFileMapTasklet;
import com.experian.saas.client.dataimport.batch.tasklet.FileAntiVirusScanTasklet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class FileAntivirusBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileRenameAndMoveConfig.class);

    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private FileAntiVirusScanTasklet fileAntiVirusScanTasklet;

    @Autowired
    private AvProcessFileMapTasklet avProcessFileMapTasklet;

    @Scheduled(fixedRate = 5000)
    public void perform() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution antivirusJob = simpleJobLauncher.run(scanFilesWithAntivirusJob(), parameters);
        LOGGER.info("Antivirus Job finished with status : {}", antivirusJob.getStatus());
    }

    @Bean
    public Step antiVirusScanStep() {
        return stepBuilderFactory.get("antiVirusScanStep")
                .tasklet(fileAntiVirusScanTasklet)
                .build();
    }

    @Bean
    public Step avProcessFileMapStep() {
        return stepBuilderFactory.get("avProcessFileMapStep")
                .tasklet(avProcessFileMapTasklet)
                .build();
    }

    @Bean
    public Job scanFilesWithAntivirusJob() {
        return jobBuilderFactory.get("scanFilesWithAntivirusJob")
                .incrementer(new RunIdIncrementer())
                .flow(avProcessFileMapStep())
                .next(antiVirusScanStep())
                .end()
                .build();
    }
}
