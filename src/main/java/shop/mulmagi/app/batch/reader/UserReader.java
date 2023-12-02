package shop.mulmagi.app.batch.reader;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shop.mulmagi.app.domain.User;
import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class UserReader {
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public ItemReader<User> userItemReader() throws Exception {
        JpaPagingItemReader<User> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT u FROM User u");
        reader.setPageSize(100);
        reader.afterPropertiesSet();
        reader.setSaveState(true);

        return reader;
    }
}
