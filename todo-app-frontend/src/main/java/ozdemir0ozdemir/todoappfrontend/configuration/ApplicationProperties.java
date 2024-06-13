package ozdemir0ozdemir.todoappfrontend.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class ApplicationProperties {

    private String authCookieName;

    /** in seconds */
    private Integer authCookieExpiration;

    private String usernameParameter;
    private String passwordParameter;
}
