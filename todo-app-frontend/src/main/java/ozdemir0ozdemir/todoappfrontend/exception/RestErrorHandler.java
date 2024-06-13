package ozdemir0ozdemir.todoappfrontend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import ozdemir0ozdemir.todoappfrontend.dto.ErrorResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RestErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }

    @Override
    public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
        ErrorResponse errorResponse = objectMapper.readValue(response.getBody().readAllBytes(), ErrorResponse.class);

        throw new LoginFailedException(errorResponse);
    }
}
