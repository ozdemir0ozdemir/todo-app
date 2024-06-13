package ozdemir0ozdemir.todoappbackend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.todoappbackend.dto.*;
import ozdemir0ozdemir.todoappbackend.service.TaskService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> saveNewTask(@Valid
                                                    @RequestBody CreateNewTaskRequest request
    ) {

        log.info("START :: Save new task requested");
        List<TaskResponse.TaskDto> tasks = this.taskService.saveNewTask(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tasks.get(0).getId())
                .toUri();

        TaskResponse response = createResponse(HttpStatus.FOUND, tasks, location.getPath());
        log.info("END :: Save new task requested");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<TaskPageResponse> findAllTasks(@Min(value = 0, message = "Page Number cannot be negative!")
                                                         @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                         @Min(value = 0, message = "Page Size cannot be negative!")
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {

        log.info("START :: Find all tasks requested");
        Page<TaskResponse.TaskDto> tasks = this.taskService.findAllTasks(pageNumber, pageSize);

        TaskResponse response = createResponse(HttpStatus.FOUND, tasks.stream().toList(), this.getPath());
        TaskPageResponse pageResponse = TaskPageResponse.builder()
                .taskResponse(response)
                .totalTaskCount(tasks.getTotalElements())
                .currentPage(pageNumber)
                .totalPage(tasks.getTotalPages())
                .build();

        log.info("END :: Find all tasks requested");
        return ResponseEntity.status(HttpStatus.FOUND).body(pageResponse);

    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> findTaskById(@Min(value = 1, message = "Task id cannot be negative!")
                                                     @PathVariable("taskId") Long taskId
    ) {

        log.info("START :: Find task by id requested");
        List<TaskResponse.TaskDto> task = this.taskService.findTaskById(taskId);

        TaskResponse response = createResponse(HttpStatus.FOUND, task, this.getPath());
        log.info("END :: Find task by id requested");
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTaskById(@Valid
                                                       @RequestBody UpdateTaskRequest request,
                                                       @Min(value = 1, message = "Task id cannot be negative!")
                                                       @PathVariable("taskId") Long taskId
    ) {

        log.info("START :: Update task by id requested");
        List<TaskResponse.TaskDto> task = this.taskService.updateTaskById(request, taskId);

        TaskResponse response = createResponse(HttpStatus.OK, task, this.getPath());
        log.info("END :: Update task by id requested");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/status/{taskId}")
    public ResponseEntity<TaskResponse> updateTaskCompletionById(@Valid
                                                                 @RequestBody UpdateTaskCompletionRequest request,
                                                                 @Min(value = 1, message = "Task id cannot be negative!")
                                                                 @PathVariable("taskId") Long taskId
    ) {

        log.info("START :: Update task completion by id requested");
        List<TaskResponse.TaskDto> task = this.taskService.updateTaskCompletionById(request, taskId);

        TaskResponse response = createResponse(HttpStatus.OK, task, this.getPath());
        log.info("END :: Update task completion by id requested");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskResponse> deleteTaskById(@Min(value = 1, message = "Task id cannot be negative!")
                                                       @PathVariable("taskId") Long taskId
    ) {

        log.info("START :: Delete task by id requested");
        List<TaskResponse.TaskDto> task = this.taskService.deleteTaskById(taskId);

        TaskResponse response = createResponse(HttpStatus.OK, task, this.getPath());
        log.info("END :: Delete task by id requested");
        return ResponseEntity.ok(response);
    }


    /**
     * @return the path request was made
     * @author Özdemir Özdemir
     */
    private String getPath() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri()
                .getPath();
    }

    /**
     * @return the path request was made with task id
     * @author Özdemir Özdemir
     */
    private String getPath(Long taskId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskId)
                .toUri()
                .getPath();
    }

    /**
     * For preventing code duplication
     *
     * @return TaskResponse
     * @author Özdemir Özdemir
     */
    private TaskResponse createResponse(HttpStatus status, List<TaskResponse.TaskDto> tasks, String path) {
        return TaskResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .tasks(tasks)
                .path(path)
                .build();
    }
}
