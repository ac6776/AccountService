package account.service.events;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

public class BruteForceEvent extends ApplicationEvent {
    public BruteForceEvent(Object source) {
        super(source);
    }

    public BruteForceEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
