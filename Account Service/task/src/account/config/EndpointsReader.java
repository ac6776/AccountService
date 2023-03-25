package account.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties
@PropertySource("classpath:endpoints.properties")
@Getter
@Setter
public class EndpointsReader {
    private Map<String, String> endpoint;
}
