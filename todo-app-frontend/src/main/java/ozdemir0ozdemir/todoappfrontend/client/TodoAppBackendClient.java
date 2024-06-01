package ozdemir0ozdemir.todoappfrontend.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ozdemir0ozdemir.todoappfrontend.dto.AuthenticationRequest;
import ozdemir0ozdemir.todoappfrontend.dto.JwtResponse;

@Service
@RequiredArgsConstructor
public class TodoAppBackendClient {

    private final RestTemplate restTemplate;

    public String getJwtAccessToken(String username, String password) {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        ResponseEntity<JwtResponse> response =
                this.restTemplate.postForEntity("http://localhost:8080/authentication", request, JwtResponse.class);
        // TODO: handle empty or unconnectable responses throwing exception
        return response.getBody().getToken();
    }
}
