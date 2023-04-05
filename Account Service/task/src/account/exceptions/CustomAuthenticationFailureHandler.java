package account.exceptions;

import account.domain.SecurityEvent;
import account.service.EventType;
import account.service.SecurityEventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityEventLogger logger;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        super.onAuthenticationFailure(request, response, exception);
//        UserDetailsImpl details = (UserDetailsImpl) request.getUserPrincipal();
        SecurityEvent event = new SecurityEvent(EventType.ACCESS_DENIED, request.getRemoteUser(), request.getContextPath(), request.getContextPath());
        logger.save(event);
    }
}
