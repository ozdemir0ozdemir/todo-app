package ozdemir0ozdemir.todoappfrontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {


    @GetMapping("/admin")
    public String index(Model model) {
        System.out.println("INDEX");
        return "admin";
    }

    @GetMapping("/accessdenied")
    private String accessDenied() {
        return "accessdenied";
    }



}
