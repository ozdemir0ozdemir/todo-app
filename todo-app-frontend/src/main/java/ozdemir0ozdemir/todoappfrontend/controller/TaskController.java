package ozdemir0ozdemir.todoappfrontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.dto.CreateNewTaskRequest;
import ozdemir0ozdemir.todoappfrontend.dto.TaskPageResponse;

@Controller
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TodoAppBackendClient backendClient;

    @GetMapping
    public String getAllTasks(@RequestParam(name = "pageNumber", required = false, defaultValue = "0") String pageNumber, Model model) {

        TaskPageResponse taskPageResponse = backendClient.getAllTasks(Integer.parseInt(pageNumber));
        model.addAttribute("tasksPage", taskPageResponse);

        model.addAttribute("newTaskRequest", new CreateNewTaskRequest());
        return "tasks";
    }

    @PostMapping
    public String saveNewTast(@ModelAttribute CreateNewTaskRequest request, BindingResult result) {
        this.backendClient.saveNewTask(request);
        return "redirect:/tasks";
    }

    @GetMapping("/{taskId}")
    public String updateTaskStatus(@PathVariable(name = "taskId") Long taskId, @RequestParam(name = "isCompleted") Boolean isCompleted, Model model) {
        this.backendClient.updateTaskStatus(taskId, isCompleted);
        return "redirect:/tasks";
    }
}
