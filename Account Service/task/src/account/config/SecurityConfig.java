package account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private UserDetailsService userService;
    private AuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public void setUserService(UserDetailsService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRestAuthenticationEntryPoint(AuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .csrf().disable().headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                    .antMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/acct/payments").permitAll()
                    .antMatchers(HttpMethod.PUT, "/api/acct/payments").permitAll()
                    .anyRequest().authenticated()
                .and()
                .userDetailsService(userService)
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
