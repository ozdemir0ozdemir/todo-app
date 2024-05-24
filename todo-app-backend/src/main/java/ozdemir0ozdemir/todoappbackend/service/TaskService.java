package ozdemir0ozdemir.todoappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.todoappbackend.dto.CreateNewTaskRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskCompletionRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskRequest;
import ozdemir0ozdemir.todoappbackend.model.Task;
import ozdemir0ozdemir.todoappbackend.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task saveNewTask(CreateNewTaskRequest request) {

        Task task = Task.builder()
                .title(request.getTitle())
                .completed(Boolean.FALSE)
                .build();

        return this.taskRepository.save(task);
    }

    public List<Task> findAllTasks() {

        return this.taskRepository.findAll();
    }

    public Task findTaskById(Long taskId) {

        // TODO: Error handling for the non-existence of the task with taskId
        return this.taskRepository.findById(taskId).get();
    }

    public Task updateTaskById(UpdateTaskRequest request, Long taskId) {

        // TODO: Error handling for the non-existence of the task with taskId
        boolean isTaskExists = this.taskRepository.findById(taskId).isPresent();
        if(isTaskExists){
            Task task = Task.builder()
                    .id(taskId)
                    .title(request.getTitle())
                    .completed(request.getCompleted())
                    .build();
            this.taskRepository.save(task);
            return task;
        }
        return Task.builder()
                .id(-999L)
                .title("Task is not exists")
                .completed(Boolean.FALSE)
                .build();
    }

    public Task updateTaskCompletionById(UpdateTaskCompletionRequest request, Long taskId) {

        // TODO: Error handling for the non-existence of the task with taskId
        Optional<Task> optionalTask = this.taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            Task task = Task.builder()
                    .id(taskId)
                    .title(optionalTask.get().getTitle())
                    .completed(request.getCompleted())
                    .build();
            this.taskRepository.save(task);
            return task;
        }
        return Task.builder()
                .id(-999L)
                .title("Task is not exists")
                .completed(Boolean.FALSE)
                .build();
    }

    public Task deleteTaskById(Long taskId) {

        // TODO: Error handling for the non-existence of the task with taskId
        Optional<Task> optionalTask = this.taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            this.taskRepository.deleteById(taskId);
            return Task.builder()
                    .id(taskId)
                    .title(optionalTask.get().getTitle())
                    .completed(optionalTask.get().getCompleted())
                    .build();
        }
        return Task.builder()
                .id(-999L)
                .title("Task is not exists")
                .completed(Boolean.FALSE)
                .build();
    }
}
