package account.controller;

import account.domain.SecurityEvent;
import account.security.UserDetailsImpl;
import account.service.SecurityEventLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityEventController {

    private SecurityEventLogger logger;

    @Autowired
    public void setLogger(SecurityEventLogger logger) {
        this.logger = logger;
    }

    Logger springLogger = LoggerFactory.getLogger(SecurityEventController.class);

    @GetMapping("${endpoint.get.security_events}")
    public List<SecurityEvent> getAllSecurityEvents(@AuthenticationPrincipal UserDetailsImpl user) {

        springLogger.info(user.getUser().toString());

        return logger.findAll();
    }
}
