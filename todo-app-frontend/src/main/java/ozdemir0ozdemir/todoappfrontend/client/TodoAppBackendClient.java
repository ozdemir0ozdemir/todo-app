package ozdemir0ozdemir.todoappfrontend.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ozdemir0ozdemir.todoappfrontend.configuration.TodoAppBackendConfiguration;
import ozdemir0ozdemir.todoappfrontend.dto.AuthenticationRequest;
import ozdemir0ozdemir.todoappfrontend.dto.JwtResponse;
import ozdemir0ozdemir.todoappfrontend.dto.MeResponse;

@Service
@RequiredArgsConstructor
public class TodoAppBackendClient {

    private final RestTemplate restTemplate;
    private final TodoAppBackendConfiguration backendConfiguration;

    public JwtResponse loginAndGetJwtToken(String username, String password) {
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();
        ResponseEntity<JwtResponse> response =
                this.restTemplate
                        .postForEntity(backendUrl+"/authentication", request, JwtResponse.class);
        // TODO: handle empty or unconnectable responses throwing exception
        return response.getBody();
    }

    public MeResponse verifyMe(String token) {

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);


        ResponseEntity<MeResponse> response = this.restTemplate
                        .exchange(backendUrl+"/authentication/me", HttpMethod.GET, entity, MeResponse.class);
        return response.getBody();
    }
}
