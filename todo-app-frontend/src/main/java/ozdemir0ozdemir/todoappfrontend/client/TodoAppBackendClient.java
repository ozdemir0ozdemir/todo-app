package ozdemir0ozdemir.todoappfrontend.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ozdemir0ozdemir.todoappfrontend.configuration.TodoAppBackendConfiguration;
import ozdemir0ozdemir.todoappfrontend.dto.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoAppBackendClient {

    private final RestTemplate restTemplate;
    private final TodoAppBackendConfiguration backendConfiguration;
    private final ObjectMapper mapper;

    public JwtResponse loginAndGetJwtToken(String username, String password) {
        AuthenticationRequest request = new AuthenticationRequest(username, password);

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();
        ResponseEntity<JwtResponse> response =
                this.restTemplate
                        .postForEntity(backendUrl+"/authentication", request, JwtResponse.class);
        // TODO: handle empty or unconnectable responses throwing exception
        return response.getBody();
    }

    public JwtResponse loginAndGetJwtToken(AuthenticationRequest request) {

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();
        ResponseEntity<JwtResponse> response =
                this.restTemplate
                        .postForEntity(backendUrl+"/authentication", request, JwtResponse.class);
        return response.getBody();
    }

    public MeResponse verifyMe(String token) {

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);


        ResponseEntity<MeResponse> response = this.restTemplate
                        .exchange(backendUrl+"/me", HttpMethod.GET, entity, MeResponse.class);
        return response.getBody();
    }

    public TaskPageResponse getAllTasks(int pageNumber) {

        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        HttpEntity<String> entity = new HttpEntity<>("body", httpHeaders);

        ResponseEntity<TaskPageResponse> response = null;
        try {
            response = this.restTemplate
                    .exchange(backendUrl+"/tasks?pageNumber="+pageNumber+"&pageSize=5", HttpMethod.GET, entity, TaskPageResponse.class);
        }
        catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
        }


        return response != null ? response.getBody() : new TaskPageResponse();
    }


    public TaskResponse updateTaskStatus(Long taskId, boolean isCompleted) {
        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        UpdateTaskCompletionRequest request = new UpdateTaskCompletionRequest(isCompleted);
        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<>( mapper.writeValueAsString(request), httpHeaders);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        ResponseEntity<TaskResponse> response = null;
        try {
            response = this.restTemplate
                    .exchange(backendUrl+"/tasks/status/"+taskId, HttpMethod.POST, entity, TaskResponse.class);

        }
        catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
        }

        return response != null ? response.getBody() : new TaskResponse();
    }

    public TaskResponse saveNewTask(CreateNewTaskRequest request) {
        final String backendUrl = backendConfiguration.getBackendUrl()+":"+backendConfiguration.getBackendPort();


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<>( mapper.writeValueAsString(request), httpHeaders);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        ResponseEntity<TaskResponse> response = null;
        try {
            response = this.restTemplate
                    .exchange(backendUrl+"/tasks", HttpMethod.POST, entity, TaskResponse.class);

        }
        catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
        }

        return response != null ? response.getBody() : new TaskResponse();
    }
}
