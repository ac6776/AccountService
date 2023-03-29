package account.domain;

import account.service.Event;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "security_events")
public class SecurityEvent {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    @Enumerated
    private Event action;
    private String subject;
    private String object;
    private String path;
}
