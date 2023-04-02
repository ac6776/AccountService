package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class UserLockedEvent extends ApplicationEvent {
    public UserLockedEvent(User user) {
        super(user);
    }
}
