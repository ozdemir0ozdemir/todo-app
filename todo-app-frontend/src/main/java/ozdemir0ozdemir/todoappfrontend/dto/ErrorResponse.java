package ozdemir0ozdemir.todoappfrontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class ErrorResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private List<ErrorItem> errors;
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
