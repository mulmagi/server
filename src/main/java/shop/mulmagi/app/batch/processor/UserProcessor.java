package shop.mulmagi.app.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import shop.mulmagi.app.domain.User;

@Component
public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(final User user) throws Exception {
        user.resetExperience();
        return user;
    }
}
