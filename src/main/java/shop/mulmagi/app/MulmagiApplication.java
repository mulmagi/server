package shop.mulmagi.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableCaching
@EnableScheduling
public class MulmagiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MulmagiApplication.class, args);
	}

}
