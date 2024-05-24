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

        Task savedTask = this.taskRepository.save(task);

        List<TaskResponse.TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskResponse.TaskDto.from(savedTask));

        return tasks;
    }

    public Page<TaskResponse.TaskDto> findAllTasks(int pageNumber, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Task> tasks = this.taskRepository.findAll(pageRequest);

        return tasks.map(TaskResponse.TaskDto::from);
    }

    public List<TaskResponse.TaskDto> findTaskById(Long taskId) {

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(taskNotFoundString, taskId)));

        List<TaskResponse.TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskResponse.TaskDto.from(task));

        return tasks;
    }

    public List<TaskResponse.TaskDto> updateTaskById(UpdateTaskRequest request, Long taskId) {

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(taskNotFoundString, taskId)));

        Task updatedTask = Task.builder()
                .id(task.getId())
                .title(request.getTitle())
                .completed(request.getCompleted())
                .build();

        this.taskRepository.saveAndFlush(updatedTask);

        List<TaskResponse.TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskResponse.TaskDto.from(updatedTask));

        return tasks;
    }

    public List<TaskResponse.TaskDto> updateTaskCompletionById(UpdateTaskCompletionRequest request, Long taskId) {

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(taskNotFoundString, taskId)));

        Task updatedTask = Task.builder()
                .id(task.getId())
                .title(task.getTitle())
                .completed(request.getCompleted())
                .build();

        this.taskRepository.saveAndFlush(updatedTask);

        List<TaskResponse.TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskResponse.TaskDto.from(updatedTask));

        return tasks;
    }

    public List<TaskResponse.TaskDto> deleteTaskById(Long taskId) {

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format(taskNotFoundString, taskId)));

        this.taskRepository.deleteById(task.getId());

        List<TaskResponse.TaskDto> tasks = new ArrayList<>();
        tasks.add(TaskResponse.TaskDto.from(task));

        return tasks;
    }

}
