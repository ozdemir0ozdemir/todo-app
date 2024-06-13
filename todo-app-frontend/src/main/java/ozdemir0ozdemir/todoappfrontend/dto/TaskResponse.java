package ozdemir0ozdemir.todoappfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private List<TaskDto> tasks;
    private String path;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class TaskDto {

        private Long id;
        private String title;
        private Boolean completed;


    }

}


