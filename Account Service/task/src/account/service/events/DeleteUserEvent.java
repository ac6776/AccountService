package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class DeleteUserEvent extends ApplicationEvent {
    public DeleteUserEvent(User user) {
        super(user);
    }
}
