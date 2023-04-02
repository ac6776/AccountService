package account.service.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class UserCreatedEvent extends ApplicationEvent {

    public UserCreatedEvent(Object source) {
        super(source);
    }

    public UserCreatedEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
