package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class RoleRemovedEvent extends ApplicationEvent {
    public RoleRemovedEvent(User user) {
        super(user);
    }
}
