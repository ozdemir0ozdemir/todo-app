package ozdemir0ozdemir.todoappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.todoappbackend.dto.CreateNewTaskRequest;
import ozdemir0ozdemir.todoappbackend.dto.TaskResponse;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskCompletionRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskRequest;
import ozdemir0ozdemir.todoappbackend.exception.TaskNotFoundException;
import ozdemir0ozdemir.todoappbackend.model.Task;
import ozdemir0ozdemir.todoappbackend.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final String taskNotFoundString = "Task with id %d is not found!";

    public List<TaskResponse.TaskDto> saveNewTask(CreateNewTaskRequest request) {

        Task task = Task.builder()
                .title(request.getTitle())
                .completed(Boolean.FALSE)
                .build();

        this.taskRepository.saveAndFlush(task);
        return List.of(TaskResponse.TaskDto.from(task));
    }

    public Page<TaskResponse.TaskDto> findAllTasks(int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = this.taskRepository.findAll(pageRequest);

        return tasks.map(TaskResponse.TaskDto::from);
    }

    public List<TaskResponse.TaskDto> findTaskById(Long taskId) {

        Task task = getTaskIfExists(taskId);

        return List.of(TaskResponse.TaskDto.from(task));
    }

    public List<TaskResponse.TaskDto> updateTaskById(UpdateTaskRequest request, Long taskId) {

        Task task = getTaskIfExists(taskId);

        task.setTitle(request.getTitle());
        task.setCompleted(request.getCompleted());

        this.taskRepository.saveAndFlush(task);
        return List.of(TaskResponse.TaskDto.from(task));
    }

    public List<TaskResponse.TaskDto> updateTaskCompletionById(UpdateTaskCompletionRequest request, Long taskId) {

        Task task = getTaskIfExists(taskId);

        task.setCompleted(request.getCompleted());

        this.taskRepository.saveAndFlush(task);
        return List.of(TaskResponse.TaskDto.from(task));
    }

    public List<TaskResponse.TaskDto> deleteTaskById(Long taskId) {

        Task task = getTaskIfExists(taskId);

        this.taskRepository.deleteById(task.getId());
        return List.of(TaskResponse.TaskDto.from(task));
    }

    private Task getTaskIfExists(Long taskId) {
        return this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(taskNotFoundString, taskId)));
    }

}
