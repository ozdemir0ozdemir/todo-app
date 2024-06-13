package ozdemir0ozdemir.todoappfrontend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ozdemir0ozdemir.todoappfrontend.exception.RestErrorHandler;


@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final ObjectMapper objectMapper;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestErrorHandler(objectMapper));


        return restTemplate;
    }


}
