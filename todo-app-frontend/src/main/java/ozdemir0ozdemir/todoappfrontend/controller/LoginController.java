package ozdemir0ozdemir.todoappfrontend.controller;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/me")
    public String mePage(Model model) {
        model.addAttribute("auth", SecurityContextHolder.getContext().getAuthentication());
        return "me";
    }



}
