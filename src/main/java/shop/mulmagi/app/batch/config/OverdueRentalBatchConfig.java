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
import shop.mulmagi.app.batch.processor.OverdueRentalProcessor;
import shop.mulmagi.app.batch.reader.OverdueRentalReader;
import shop.mulmagi.app.batch.writer.OverdueRentalWriter;
import shop.mulmagi.app.domain.Rental;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class OverdueRentalBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final OverdueRentalReader overdueRentalReader;
    private final OverdueRentalProcessor overdueRentalProcessor;
    private final OverdueRentalWriter overdueRentalWriter;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    @Bean
    public Job overdueRentalBatchJob() throws Exception {
        return jobBuilderFactory.get("calculateOverdueBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(calculateOverdueStep())
                .build();
    }

    @Bean
    public Step calculateOverdueStep() throws Exception {
        return stepBuilderFactory.get("calculateOverdueStep")
                .allowStartIfComplete(true)
                .<Rental, Rental>chunk(chunkSize)
                .reader(overdueRentalReader.rentalItemReader())
                .processor(overdueRentalProcessor)
                .writer(overdueRentalWriter)
                .faultTolerant() // Skip 및 Retry를 사용하도록 설정
                .skip(Exception.class)
                .skipLimit(1000) // 허용되는 최대 건너뛰기 수
                .build();
    }

}