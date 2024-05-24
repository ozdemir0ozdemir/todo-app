package ozdemir0ozdemir.todoappbackend.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import ozdemir0ozdemir.todoappbackend.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private Collection<TaskDto> tasks;
    private String path;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class TaskDto {

        private Long id;
        private String title;
        private Boolean completed;

        public static TaskDto from(Task task){

            return TaskDto.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .completed(task.getCompleted())
                    .build();
        }

    }

}


