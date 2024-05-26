package ozdemir0ozdemir.todoappbackend.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfiguration {

    /** Secret key to sign and verify jwt token */
    private String secret;

    /** Expiration time in millisecond for jwt token */
    private Integer expiration;

}
