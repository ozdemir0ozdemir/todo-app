package ozdemir0ozdemir.todoappfrontend.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "backend")
@Data
public class TodoAppBackendConfiguration {

    private String backendUrl;
    private String backendPort;

}
