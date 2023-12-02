package shop.mulmagi.app.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mulmagi.app.batch.processor.UserProcessor;
import shop.mulmagi.app.batch.reader.UserReader;
import shop.mulmagi.app.batch.writer.UserWriter;
import shop.mulmagi.app.domain.User;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class UserExperienceBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserReader userReader;
    private final UserProcessor userProcessor;
    private final UserWriter userWriter;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean
    public Job resetUserExperienceBatchJob() throws Exception {
        return jobBuilderFactory.get("resetUserExperienceBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(resetUserExperienceStep())
                .build();
    }

    @Bean
    public Step resetUserExperienceStep() throws Exception {
        return stepBuilderFactory.get("resetUserExperienceStep")
                .allowStartIfComplete(true)
                .<User, User>chunk(chunkSize)
                .reader(userReader.userItemReader())
                .processor(userProcessor)
                .writer(userWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }

}