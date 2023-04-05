package account.service;

import account.domain.SecurityEvent;
import account.repository.SecurityEventRepository;
import account.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

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

//    @EventListener
//    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failureEvent) {
////        failureEvent.getException().getMessage();
////        SecurityEvent securityEvent = new SecurityEvent();
////        securityEvent.setAction(Event.LOGIN_FAILED);
////        securityEvent.setSubject("Anonymous");
////        securityEvent.setObject(((User) failureEvent.getSource()).getEmail());
////        securityEvent.setPath("/api/auth/signup");
////        securityEventRepository.save(securityEvent);
//    }

    @EventListener
    public void onSecurityEvent(ApplicationSecurityEvent event) {
        securityEventRepository.save(event.getSecurityEvent());
    }

//    @EventListener
//    public void onUserCreated(ApplicationSecurityEvent event) {
////        SecurityEvent securityEvent = new SecurityEvent();
////        securityEvent.setAction(event.getType());
////        securityEvent.setSubject("Anonymous"); //user details
////        securityEvent.setObject(((User) event.getSource()).getEmail()); //user
////        securityEvent.setPath("/api/auth/signup");
////        securityEventRepository.save(securityEvent);
//    }

//    @EventListener
//    public void onChangePassword(ChangePasswordEvent event) {
//
//    }
//
//    @EventListener
//    public void onRoleGranted(RoleGrantedEvent event) {
//
//    }
//
//    @EventListener
//    public void onRoleRemoved(RoleRemovedEvent event) {
//
//    }
//
//    @EventListener
//    public void onDeleteUser(DeleteUserEvent event) {
//
//    }
//
//    @EventListener
//    public void onUserLocked(UserLockedEvent event) {
//
//    }
//
//    @EventListener
//    public void onUserUnlocked(UserUnlockedEvent event) {
//
//    }
//
//    @EventListener
//    public void onBruteForce(BruteForceEvent event) {
//
//    }
}
