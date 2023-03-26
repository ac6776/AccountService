package account.security;

import account.config.EndpointsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.signup")).permitAll();
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.shutdown")).permitAll();
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.changepass")).authenticated();
                    auth.antMatchers(HttpMethod.GET, getEndpoint("get.payment")).hasAnyRole("USER", "ACCOUNTANT");
                    auth.antMatchers(HttpMethod.POST, getEndpoint("post.payments")).hasRole("ACCOUNTANT");
                    auth.antMatchers(HttpMethod.PUT, getEndpoint("put.payments")).hasRole("ACCOUNTANT");
                    auth.antMatchers(HttpMethod.GET, getEndpoint("get.user")).hasRole("ADMINISTRATOR");
                    auth.antMatchers(HttpMethod.DELETE, getEndpoint("delete.user")).hasRole("ADMINISTRATOR");
                    auth.antMatchers(HttpMethod.PUT, getEndpoint("put.role")).hasRole("ADMINISTRATOR");
                    auth.anyRequest().authenticated();
                })
                .userDetailsService(userService)
                .csrf(CsrfConfigurer::disable)
//                .headers(conf -> conf.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(conf -> conf.authenticationEntryPoint(restAuthenticationEntryPoint));
        return http.build();
    }

    private String getEndpoint(String key) {
        return endpointsReader.getEndpoint().get(key);
    }
}
