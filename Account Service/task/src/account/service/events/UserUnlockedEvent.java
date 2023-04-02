package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class UserUnlockedEvent extends ApplicationEvent {
    public UserUnlockedEvent(User user) {
        super(user);
    }
}
