package ozdemir0ozdemir.todoappbackend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.todoappbackend.dto.CreateNewTaskRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskCompletionRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskRequest;
import ozdemir0ozdemir.todoappbackend.model.Task;
import ozdemir0ozdemir.todoappbackend.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> saveNewTask(@Valid @RequestBody CreateNewTaskRequest request) {

        Task task = this.taskService.saveNewTask(request);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public List<Task> findAllTasks() {

        return this.taskService.findAllTasks();
    }

    @GetMapping("/{taskId}")
    public Task findTaskById(@Min(value = 1, message = "Task id cannot be negative")
                             @PathVariable("taskId") Long taskId) {

        return this.taskService.findTaskById(taskId);
    }

    @PutMapping("/{taskId}")
    public Task updateTaskById(@Valid @RequestBody UpdateTaskRequest request,

                               @Min(value = 1, message = "Task id cannot be negative")
                               @PathVariable("taskId") Long taskId) {

        return this.taskService.updateTaskById(request, taskId);
    }

    @PatchMapping("/{taskId}")
    public Task updateTaskCompletionById(@Valid @RequestBody UpdateTaskCompletionRequest request,

                                         @Min(value = 1, message = "Task id cannot be negative")
                                         @PathVariable("taskId") Long taskId) {

        return this.taskService.updateTaskCompletionById(request, taskId);
    }

    @DeleteMapping("/{taskId}")
    public Task deleteTaskById(@Min(value = 1, message = "Task id cannot be negative")
                               @PathVariable("taskId") Long taskId) {

        return this.taskService.deleteTaskById(taskId);
    }


}
