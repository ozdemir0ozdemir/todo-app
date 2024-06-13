package ozdemir0ozdemir.todoappfrontend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.dto.AuthenticationRequest;
import ozdemir0ozdemir.todoappfrontend.dto.ErrorResponse;
import ozdemir0ozdemir.todoappfrontend.exception.LoginFailedException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final TodoAppBackendClient backendClient;

    // force login page
    @GetMapping("/login")
    public String loginPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && !authentication.getClass().equals(AnonymousAuthenticationToken.class)){
            return "redirect:/me";
        }

        model.addAttribute("authRequest", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute AuthenticationRequest authenticationRequest, Model model, HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("TodoAppCookie", backendClient.loginAndGetJwtToken(authenticationRequest).getToken());
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(18000);

            response.addCookie(cookie);
        }
        catch (LoginFailedException ex) {
            model.addAttribute("authRequest", authenticationRequest);

            String error = ex.getErrorResponse()
                    .getErrors()
                    .stream()
                    .map(ErrorResponse.ErrorItem::getMessage)
                    .reduce(String::concat)
                    .get();

            model.addAttribute("error", error);
            return "/login";
        }

        return "redirect:/me";
    }

    @GetMapping("/me")
    public String mePage(Model model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("ME PAGE");
        model.addAttribute("auth", SecurityContextHolder.getContext().getAuthentication());
        return "me";
    }

    @PostMapping("/logout")
    public String loginProcess(HttpServletRequest request, HttpServletResponse response){
        System.out.println(":::LOGOUT");
        return "redirect:/login";
    }

}
