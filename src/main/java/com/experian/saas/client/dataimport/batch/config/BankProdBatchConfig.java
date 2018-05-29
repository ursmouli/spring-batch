package com.experian.saas.client.dataimport.batch.config;

import com.experian.saas.client.dataimport.batch.appsql.AppSqlQuery;
import com.experian.saas.client.dataimport.batch.components.ConfigParameters;
import com.experian.saas.client.dataimport.batch.listener.BatchChunkListener;
import com.experian.saas.client.dataimport.batch.listener.JobCompletionNotificationListener;
import com.experian.saas.client.dataimport.batch.logger.ResourceLoggingItemWriter;
import com.experian.saas.client.dataimport.batch.model.Account;
import com.experian.saas.client.dataimport.batch.model.Customer;
import com.experian.saas.client.dataimport.batch.processor.AccountItemProcessor;
import com.experian.saas.client.dataimport.batch.processor.CustomerItemProcessor;
import com.experian.saas.client.dataimport.batch.scheduler.BatchScheduler;
import com.experian.saas.client.dataimport.batch.tasklet.FileAntiVirusScanTasklet;
import com.experian.saas.client.dataimport.batch.tasklet.ZipArchivedFilesTasklet;
import com.experian.saas.client.dataimport.batch.util.ResourceUtil;
import com.experian.saas.client.dataimport.batch.validator.JobParameterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@Import({ BatchScheduler.class })
public class BankProdBatchConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankProdBatchConfig.class);

    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ConfigParameters configParameters;

    @Autowired
    @Qualifier("postgresDataSource")
    private DataSource postgresDataSource;

    @Autowired
    private FileAntiVirusScanTasklet fileAntiVirusScanTasklet;

    @Autowired
    private ZipArchivedFilesTasklet zipArchivedFilesTasklet;

    @Scheduled(fixedRate = 5000)
    public void perform() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();

        JobExecution antivirusJob = simpleJobLauncher.run(scanFilesWithAntivirusJob(), parameters);
        LOGGER.info("Antivirus Job finished with status : {}", antivirusJob.getStatus());

        JobExecution fileProcessJob = simpleJobLauncher.run(readImportFilesJob(), parameters);
        LOGGER.info("File process Job finished with status : {}", fileProcessJob.getStatus());

        JobExecution zipArchiveJob = simpleJobLauncher.run(zipArchivedFilesJob(), parameters);
        LOGGER.info("ZIP archive Job finished with status : {}", zipArchiveJob.getStatus());
    }

    // Account Reader/Writer/Processor
    @Bean
    @StepScope
    public MultiResourceItemReader<Account> accountReader() {
        FlatFileItemReader<Account> delegateReader = new FlatFileItemReader<Account>();
        delegateReader.setResource(new FileSystemResource(configParameters.getFileProcessDirPath()));
        delegateReader.setLinesToSkip(1);
        delegateReader.setLineMapper(new DefaultLineMapper<Account>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(AppSqlQuery.accountDtoMappingNames());
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Account>() {{
                setTargetType(Account.class);
            }});
        }});

        Resource[] resources = ResourceUtil.readResources(configParameters.getFileProcessDirPath(), "account");

        MultiResourceItemReader<Account> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.setDelegate(delegateReader);

        return multiResourceItemReader;
    }

    @Bean
    public ItemWriter<Account> accountWriter() {
        JdbcBatchItemWriter<Account> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(AppSqlQuery.insertAccount());
        writer.setDataSource(postgresDataSource);

        return new ResourceLoggingItemWriter<>(writer, configParameters.getArchivePath());
    }

    @Bean
    public AccountItemProcessor accountItemProcessor() {
        return new AccountItemProcessor();
    }

    @Bean
    public Step processAccountStep() {
        return stepBuilderFactory.get("processAccountStep")
                .<Account, Account> chunk(10)
                .reader(accountReader())
                .processor(accountItemProcessor())
                .writer(accountWriter())
                //.listener(batchStepChunkListener())
                .build();
    }
    // End - Account Reader/Writer/Processor

    // Customer Reader/Writer/Processor
    @Bean
    @StepScope
    public MultiResourceItemReader<Customer> customerItemReader() {
        FlatFileItemReader<Customer> delegateReader = new FlatFileItemReader<>();
        delegateReader.setResource(new FileSystemResource(configParameters.getFileProcessDirPath()));
        delegateReader.setLinesToSkip(1);
        delegateReader.setLineMapper(new DefaultLineMapper<Customer>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(AppSqlQuery.customerDtoMappingNames());
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                setTargetType(Customer.class);
            }});
        }});

        Resource[] resources = ResourceUtil.readResources(configParameters.getFileProcessDirPath(), "customer");

        MultiResourceItemReader<Customer> multiResourceItemReader = new MultiResourceItemReader<>();
        multiResourceItemReader.setResources(resources);
        multiResourceItemReader.setDelegate(delegateReader);

        return multiResourceItemReader;
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql(AppSqlQuery.insertCustomer());
        writer.setDataSource(postgresDataSource);

        return new ResourceLoggingItemWriter<>(writer, configParameters.getArchivePath());
    }

    @Bean
    public CustomerItemProcessor customerItemProcessor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public Step processCustomerStep() {
        return stepBuilderFactory.get("processCustomerStep")
                .<Customer, Customer> chunk(10)
                .reader(customerItemReader())
                .processor(customerItemProcessor())
                .writer(customerItemWriter())
                //.listener(batchStepChunkListener())
                .build();
    }
    // End - Customer Reader/Writer/Processor

    // Files AntiVirus scan
    @Bean
    public Step antiVirusScanStep() {
        return stepBuilderFactory.get("antiVirusScanStep")
                .tasklet(fileAntiVirusScanTasklet)
                .build();
    }
    // End - Files AntiVirus scan

    // ZIP archived files
    @Bean
    public Step zipArchivedFilesStep() {
        return stepBuilderFactory.get("zipArchivedFilesStep")
                .tasklet(zipArchivedFilesTasklet)
                .build();
    }
    // End - ZIP archived files

    @Bean
    public ChunkListener batchStepChunkListener() {
        return new BatchChunkListener();
    }

    /**
     * Can configure decision oriented step as below.
     * ----------------------------------------------
     * .flow(processCustomerStep())
     *      .on("*")
     *      .to(processAccountStep())
     * .from(processCustomerStep())
     *      .on("FAILED")
     *      .to(processAccountStep())
     * -----------------------------------------------
     * @return
     */
    @Bean
    public Job readImportFilesJob() {
        return jobBuilderFactory.get("readImportFilesJob")
                .incrementer(new RunIdIncrementer())
                .validator(jobParametersValidator())
                .listener(jobCompletionNotificationListener())
                .flow(processCustomerStep())
                    .next(processAccountStep())
                .end().build();
    }

    @Bean
    public Job scanFilesWithAntivirusJob() {
        return jobBuilderFactory.get("scanFilesWithAntivirusJob")
                .incrementer(new RunIdIncrementer())
                .flow(antiVirusScanStep())
                .end()
                .build();
    }

    @Bean
    public Job zipArchivedFilesJob() {
        return jobBuilderFactory.get("zipArchivedFilesJob")
                .incrementer(new RunIdIncrementer())
                .flow(zipArchivedFilesStep())
                .end()
                .build();
    }

    @Bean
    public JobParametersValidator jobParametersValidator() {
        return new JobParameterValidator();
    }

    @Bean
    public JobCompletionNotificationListener jobCompletionNotificationListener() {
        return new JobCompletionNotificationListener();
    }
}
