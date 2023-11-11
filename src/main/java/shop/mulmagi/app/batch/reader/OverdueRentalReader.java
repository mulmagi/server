package shop.mulmagi.app.batch.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mulmagi.app.domain.Rental;
import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class OverdueRentalReader {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<Rental> rentalItemReader() throws Exception {
        JpaPagingItemReader<Rental> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT r FROM Rental r WHERE r.isReturn = false");
        reader.setPageSize(100);
        reader.afterPropertiesSet();

        return reader;
    }
}
