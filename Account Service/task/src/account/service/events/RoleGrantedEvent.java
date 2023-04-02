package account.service.events;

import account.domain.User;
import org.springframework.context.ApplicationEvent;

public class RoleGrantedEvent extends ApplicationEvent {
    public RoleGrantedEvent(User user) {
        super(user);
    }
}
