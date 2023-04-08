package account.service;

import account.domain.SecurityEvent;
import account.repository.SecurityEventRepository;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class SecurityEventLogger {

    private SecurityEventRepository securityEventRepository;
    private UserService userService;

    @Autowired
    public void setSecurityEventRepository(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<SecurityEvent> findAll() {
        return securityEventRepository.findAll();
    }

    public void save(SecurityEvent event) {
        securityEventRepository.save(event);
    }


    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        var user = ((UserDetailsImpl) event.getAuthentication().getPrincipal()).getUser();
        userService.resetLoginAttempts(user);
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent failureEvent) {
//        User user = ((UserDetailsImpl)failureEvent.getAuthentication().getPrincipal()).getUser();
        String path = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getServletPath();
        String email = failureEvent.getAuthentication().getPrincipal().toString();
        securityEventRepository.save(new SecurityEvent(EventType.LOGIN_FAILED, email, path, path));
        userService.incrementLoginAttempts(email, path);
    }

    @EventListener
    public void onSecurityEvent(ApplicationSecurityEvent event) {
        securityEventRepository.save(event.getSecurityEvent());
    }

}
