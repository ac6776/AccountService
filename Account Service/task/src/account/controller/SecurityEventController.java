package account.controller;

import account.domain.SecurityEvent;
import account.service.SecurityEventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityEventController {
    @Autowired
    private SecurityEventLogger logger;

    @GetMapping("${endpoint.get.security_events}")
    public List<SecurityEvent> getAllSecurityEvents() {
        return logger.findAll();
    }
}
