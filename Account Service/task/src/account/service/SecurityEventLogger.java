package account.service;

import account.domain.SecurityEvent;
import account.domain.User;
import account.repository.SecurityEventRepository;
import account.service.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityEventLogger {

    private SecurityEventRepository securityEventRepository;

    @Autowired
    public void setSecurityEventRepository(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    public List<SecurityEvent> findAll() {
        return securityEventRepository.findAll();
    }

    public void save(SecurityEvent event) {
        securityEventRepository.save(event);
    }

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("user Created: " + ((User) event.getSource()).getEmail());
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        System.out.println(event.getClass());
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failureEvent) {
        System.out.println("failure");
    }

    @EventListener
    public void onAccessDenied(AuthorizationDeniedEvent event) {
        System.out.println("Authorization denied");
    }

    @EventListener
    public void onChangePassword(ChangePasswordEvent event) {

    }

    @EventListener
    public void onRoleGranted(RoleGrantedEvent event) {

    }

    @EventListener
    public void onRoleRemoved(RoleRemovedEvent event) {

    }

    @EventListener
    public void onDeleteUser(DeleteUserEvent event) {

    }

    @EventListener
    public void onUserLocked(UserLockedEvent event) {

    }

    @EventListener
    public void onUserUnlocked(UserUnlockedEvent event) {

    }

    @EventListener
    public void onBruteForce(BruteForceEvent event) {

    }
}
