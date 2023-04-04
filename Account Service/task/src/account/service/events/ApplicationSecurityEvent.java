package account.service.events;

import account.service.Event;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class ApplicationSecurityEvent extends ApplicationEvent {

    private Event type;

    public ApplicationSecurityEvent(Object source) {
        super(source);
    }

    public ApplicationSecurityEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public ApplicationSecurityEvent(Object source, Event type) {
        super(source);
        this.type = type;
    }

    public Event getType() {
        return type;
    }
}
