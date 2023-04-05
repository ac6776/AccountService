package account.domain;

import account.service.EventType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.ApplicationEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.EventObject;

@Entity
@Getter
@Setter
@Table(name = "security_events")
public class SecurityEvent {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime date;
    @Enumerated
    private EventType action;
    private String subject;
    private String object;
    private String path;
    @Transient
    private Object source;


    public SecurityEvent(EventType event, String subject, String object, String path) {
        this.action = event;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}
