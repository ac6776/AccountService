package account.service;

import account.domain.SecurityEvent;
import org.springframework.context.ApplicationEvent;

public class ApplicationSecurityEvent extends ApplicationEvent {

    private SecurityEvent securityEvent;

    public ApplicationSecurityEvent(Object source) {
        super(source);
    }

    public ApplicationSecurityEvent(Object source, SecurityEvent event) {
        super(source);
        this.securityEvent = event;
    }

    public SecurityEvent getSecurityEvent() {
        return securityEvent;
    }
}
