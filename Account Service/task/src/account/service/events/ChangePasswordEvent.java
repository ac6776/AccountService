package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class ChangePasswordEvent extends ApplicationEvent {
    public ChangePasswordEvent(User user) {
        super(user);
    }
}
