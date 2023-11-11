package shop.mulmagi.app.batch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import shop.mulmagi.app.batch.processor.OverdueRentalProcessor;
import shop.mulmagi.app.batch.reader.OverdueRentalReader;
import shop.mulmagi.app.batch.writer.OverdueRentalWriter;
import shop.mulmagi.app.domain.Rental;
import shop.mulmagi.app.domain.User;
import javax.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class OverdueRentalConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final OverdueRentalReader overdueRentalReader;
    private final OverdueRentalProcessor overdueRentalProcessor;
    private final OverdueRentalWriter overdueRentalWriter;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean
    public Job overdueRentalBatchJob() throws Exception {
        return jobBuilderFactory.get("overdueRentalBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .allowStartIfComplete(true)
                .<Rental, Rental>chunk(chunkSize)
                .reader(overdueRentalReader.rentalItemReader())
                .processor(overdueRentalProcessor)
                .writer(overdueRentalWriter)
                .build();
    }

}