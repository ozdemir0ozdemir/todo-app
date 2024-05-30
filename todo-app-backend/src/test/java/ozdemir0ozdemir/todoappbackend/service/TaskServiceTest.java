package ozdemir0ozdemir.todoappbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ozdemir0ozdemir.todoappbackend.dto.CreateNewTaskRequest;
import ozdemir0ozdemir.todoappbackend.dto.TaskResponse;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskCompletionRequest;
import ozdemir0ozdemir.todoappbackend.dto.UpdateTaskRequest;
import ozdemir0ozdemir.todoappbackend.exception.TaskNotFoundException;
import ozdemir0ozdemir.todoappbackend.model.Task;
import ozdemir0ozdemir.todoappbackend.repository.TaskRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void saveNewTaskTest() {

        // Given
        String title = "CreateNewTaskRequest Title";
        CreateNewTaskRequest request = new CreateNewTaskRequest(title);


        // When
        when(this.taskRepository.saveAndFlush(Mockito.any(Task.class))).thenReturn(Mockito.any());
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        List<TaskResponse.TaskDto> dtos = this.taskService.saveNewTask(request);


        //Then
        Mockito.verify(this.taskRepository, Mockito.times(1))
                .saveAndFlush(taskCaptor.capture());

        assertEquals(taskCaptor.getValue().getTitle(), request.getTitle());
        assertFalse(taskCaptor.getValue().getCompleted());

        assertEquals(request.getTitle(), dtos.get(0).getTitle());
        assertFalse(dtos.get(0).getCompleted());


    }

    @Test
    void findAllTasksTest() {

        // Given
        final int pageNumber = 0;
        final int pageSize = 10;
        final long id = 1L;
        final String title = "Task Title";

        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(Boolean.FALSE)
                .build();

        Page<Task> page = new PageImpl<>(List.of(task));

        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);

        // When
        when(this.taskRepository.findAll(pageRequestArgumentCaptor.capture())).thenReturn(page);

        // Then
        Page<TaskResponse.TaskDto> dtos = this.taskService.findAllTasks(pageNumber, pageSize);

        Mockito.verify(this.taskRepository, Mockito.times(1))
                .findAll(Mockito.any(PageRequest.class));

        assertNotNull(dtos);

        assertEquals(pageNumber, pageRequestArgumentCaptor.getValue().getPageNumber());
        assertEquals(pageSize, pageRequestArgumentCaptor.getValue().getPageSize());

        TaskResponse.TaskDto returnedDto = dtos.stream().findFirst().get();

        assertEquals(task.getId(), returnedDto.getId());
        assertEquals(task.getTitle(), returnedDto.getTitle());
        assertEquals(task.getCompleted(), returnedDto.getCompleted());

    }

    @Test
    void findTaskByIdTest() {
        // Given
        final long id = 1L;
        final String title = "Task Title";

        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(Boolean.FALSE)
                .build();

        // When
        when(this.taskRepository.findById(id)).thenReturn(Optional.of(task));

        // Then
        List<TaskResponse.TaskDto> dtos = this.taskService.findTaskById(id);

        Mockito.verify(this.taskRepository, times(1)).findById(1L);

        assertNotNull(dtos);
        assertNotNull(dtos.get(0));
        assertEquals(task.getId(), dtos.get(0).getId());
        assertEquals(task.getTitle(), dtos.get(0).getTitle());
        assertEquals(task.getCompleted(), dtos.get(0).getCompleted());
    }

    @Test
    void updateTaskByIdTest() {

        // Given
        final long id = 1L;
        final String title = "Task Title";

        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(Boolean.FALSE)
                .build();

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);

        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(title, Boolean.FALSE);

        // When
        when(this.taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(this.taskRepository.saveAndFlush(taskArgumentCaptor.capture())).thenReturn(task);

        // Then
        List<TaskResponse.TaskDto> dtos = this.taskService.updateTaskById(updateTaskRequest, id);

        Mockito.verify(this.taskRepository, times(1)).findById(id);
        Mockito.verify(this.taskRepository, times(1)).saveAndFlush(Mockito.any(Task.class));

        assertEquals(updateTaskRequest.getTitle(), taskArgumentCaptor.getValue().getTitle());
        assertEquals(updateTaskRequest.getCompleted(), taskArgumentCaptor.getValue().getCompleted());

        assertEquals(task.getId(), dtos.get(0).getId());
        assertEquals(task.getTitle(), dtos.get(0).getTitle());
        assertEquals(task.getCompleted(), dtos.get(0).getCompleted());
    }

    @Test
    void updateTaskCompletionByIdTest() {

        // Given
        final long id = 1L;
        final String title = "Task Title";

        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(Boolean.TRUE)
                .build();

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);

        UpdateTaskCompletionRequest updateTaskCompletionRequest = new UpdateTaskCompletionRequest(Boolean.FALSE);

        // When
        when(this.taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(this.taskRepository.saveAndFlush(taskArgumentCaptor.capture())).thenReturn(task);

        // Then
        List<TaskResponse.TaskDto> dtos = this.taskService.updateTaskCompletionById(updateTaskCompletionRequest, id);

        Mockito.verify(this.taskRepository, times(1)).findById(id);
        Mockito.verify(this.taskRepository, times(1)).saveAndFlush(Mockito.any(Task.class));

        assertEquals(updateTaskCompletionRequest.getCompleted(), taskArgumentCaptor.getValue().getCompleted());

        assertEquals(task.getId(), dtos.get(0).getId());
        assertEquals(task.getTitle(), dtos.get(0).getTitle());
        assertEquals(task.getCompleted(), dtos.get(0).getCompleted());
    }

    @Test
    void deleteTaskByIdTest() {

        // Given
        final long id = 1L;
        final String title = "Task Title";
        final Boolean completion = Boolean.TRUE;

        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(completion)
                .build();

        ArgumentCaptor<Long> taskIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        UpdateTaskCompletionRequest updateTaskCompletionRequest = new UpdateTaskCompletionRequest(completion);

        // When
        when(this.taskRepository.findById(id)).thenReturn(Optional.of(task));
        doNothing().when(this.taskRepository).deleteById(taskIdArgumentCaptor.capture());

        // Then
        List<TaskResponse.TaskDto> dtos = this.taskService.deleteTaskById(id);

        Mockito.verify(this.taskRepository, times(1)).findById(id);
        Mockito.verify(this.taskRepository, times(1)).deleteById(id);

        assertEquals(id, taskIdArgumentCaptor.getValue());
        assertEquals(updateTaskCompletionRequest.getCompleted(), dtos.get(0).getCompleted());

        assertEquals(task.getId(), dtos.get(0).getId());
        assertEquals(task.getTitle(), dtos.get(0).getTitle());
        assertEquals(task.getCompleted(), dtos.get(0).getCompleted());
    }



    @Test
    void getTaskIfExistsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = this.taskService.getClass().getDeclaredMethod("getTaskIfExists", Long.class);
        method.setAccessible(true);
        // Given
        final long id = 1L;
        final String title = "Task Title";


        Task task = Task.builder()
                .id(id)
                .title(title)
                .completed(Boolean.FALSE)
                .build();

        // When
        when(this.taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(this.taskRepository.findById(2L)).thenReturn(Optional.empty());

        // Then
        Task mustReturnValidTask = (Task) method.invoke(this.taskService, 1L);

        assertNotNull(mustReturnValidTask);
        assertEquals(task.getId(), mustReturnValidTask.getId());
        assertEquals(task.getTitle(), mustReturnValidTask.getTitle());
        assertEquals(task.getCompleted(), mustReturnValidTask.getCompleted());

        InvocationTargetException exception =
                assertThrows(InvocationTargetException.class, () -> method.invoke(this.taskService, 2L));


        assertEquals(TaskNotFoundException.class, exception.getCause().getClass());
        assertEquals("Task with id 2 is not found!", exception.getCause().getMessage());

        method.setAccessible(false);
    }

}