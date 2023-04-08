package account.domain;

import account.service.EventType;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
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
    @JsonIgnore
    private String additionalData;

    public SecurityEvent(EventType event, String subject, String object, String path) {
        this.action = event;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    @JsonGetter("object")
    public String getObjectFormatted() {
        if (action == EventType.GRANT_ROLE) {
            return "Grant role %s to %s".formatted(additionalData.substring("ROLE_".length()), object);
        }
        if (action == EventType.REMOVE_ROLE) {
            return "Remove role %s from %s".formatted(additionalData.substring("ROLE_".length()), object);
        }
        return object;
    }
}
