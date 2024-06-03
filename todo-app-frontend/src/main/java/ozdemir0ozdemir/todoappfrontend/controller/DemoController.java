package ozdemir0ozdemir.todoappfrontend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;

@Controller
@RequiredArgsConstructor
public class DemoController {

    private final TodoAppBackendClient backendClient;

    @GetMapping("/addCookie")
    public String index(Model model, HttpServletResponse response) {

        String jwt = "token token token";
        model.addAttribute("jwt", jwt);

        Cookie cookie = new Cookie("TodoAppToken", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("http://localhost:8081");
        cookie.setMaxAge(180);

        response.addCookie(cookie);
        return "index";
    }



}
