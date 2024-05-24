package ozdemir0ozdemir.todoappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ErrorResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private Collection<ErrorItem> errors;
    private String path;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ErrorItem{
        String message;
        String help;
    }
}
