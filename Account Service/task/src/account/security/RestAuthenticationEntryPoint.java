package account.security;

import account.domain.SecurityEvent;
import account.domain.User;
import account.messages.CustomErrorMessage;
import account.service.EventType;
import account.service.SecurityEventLogger;
import account.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private SecurityEventLogger logger;
    @Autowired
    private UserService userService;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        CustomErrorMessage body = new CustomErrorMessage(
                LocalDateTime.now().toString(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "",
                request.getServletPath()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);

        User user = ((UserDetailsImpl)request.getUserPrincipal()).getUser();
        userService.incrementLoginAttempts(user, request.getServletPath());
        logger.save(new SecurityEvent(EventType.LOGIN_FAILED, request.getRemoteUser(), request.getContextPath(), request.getContextPath()));
    }
}
