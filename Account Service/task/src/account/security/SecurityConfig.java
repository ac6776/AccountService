package account.security;

import account.config.EndpointsReader;
import account.exceptions.AccessDeniedExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authorization.AuthorizationEventPublisher;
import org.springframework.security.authorization.SpringAuthorizationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {
    private UserDetailsService userService;
    private AuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private EndpointsReader endpointsReader;

    @Autowired
    public void setUserService(UserDetailsService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRestAuthenticationEntryPoint(AuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationEventPublisher authenticationEventPublisher) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.antMatchers("/test").permitAll();
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.signup")).permitAll();
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.shutdown")).permitAll();
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.changepass")).authenticated();
                    auth.antMatchers(HttpMethod.GET, getEndpoint("get.security_events")).hasRole("AUDITOR");
//                    auth.antMatchers(HttpMethod.GET, getEndpoint("get.security_events")).permitAll();
                    auth.antMatchers(HttpMethod.GET, getEndpoint("get.payment")).hasAnyRole("USER", "ACCOUNTANT");
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.payments")).hasRole("ACCOUNTANT");
                    auth.antMatchers(HttpMethod.PUT, getEndpoint("put.payments")).hasRole("ACCOUNTANT");
                    auth.antMatchers(getEndpoint("get.user") + "/**").hasRole("ADMINISTRATOR");
                    auth.antMatchers(HttpMethod.PUT, getEndpoint("put.role")).hasRole("ADMINISTRATOR");
                    auth.antMatchers(HttpMethod.PUT, getEndpoint("put.access")).hasRole("ADMINISTRATOR");
                    auth.antMatchers(HttpMethod.DELETE, getEndpoint("delete.user") + "/**").hasRole("ADMINISTRATOR");
                    auth.anyRequest().authenticated();
                })
                .userDetailsService(userService)
                .csrf(CsrfConfigurer::disable)
                .headers(conf -> conf.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(conf -> conf.accessDeniedHandler(accessDeniedHandler()))
                .httpBasic(conf -> conf.authenticationEntryPoint(restAuthenticationEntryPoint))
                .getSharedObject(AuthenticationManagerBuilder.class).authenticationEventPublisher(authenticationEventPublisher);
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedExceptionHandler();
    }



    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher
            (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    private String getEndpoint(String key) {
        return endpointsReader.getEndpoint().get(key);
    }

}
