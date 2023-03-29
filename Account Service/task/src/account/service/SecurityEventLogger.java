package account.service;

import account.repository.SecurityEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityEventLogger {

    private SecurityEventRepository securityEventRepository;

    @Autowired
    public void setSecurityEventRepository(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }
}
