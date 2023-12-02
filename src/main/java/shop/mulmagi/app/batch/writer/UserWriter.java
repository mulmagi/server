package shop.mulmagi.app.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.User;
import shop.mulmagi.app.repository.UserRepository;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserWriter implements ItemWriter<User> {
    private final UserRepository userRepository;

    @Override
    public void write(List<? extends User> users) {
        userRepository.saveAll(users);
    }
}