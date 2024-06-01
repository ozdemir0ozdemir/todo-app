package ozdemir0ozdemir.todoappfrontend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;

@Controller
@RequiredArgsConstructor
public class DemoController {

    private final TodoAppBackendClient backendClient;

    @GetMapping({"", "/"})
    public String index(Model model) {
        String jwt = backendClient.getJwtAccessToken("admin", "password");
        model.addAttribute("jwt", jwt);
        return "index";
    }
}
