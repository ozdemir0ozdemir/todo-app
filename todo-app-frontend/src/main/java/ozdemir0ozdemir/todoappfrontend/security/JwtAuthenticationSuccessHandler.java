package ozdemir0ozdemir.todoappfrontend.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ozdemir0ozdemir.todoappfrontend.dto.JwtResponse;

import java.io.IOException;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Cookie cookie = new Cookie("TodoAppToken", ((JwtResponse) authentication.getDetails()).getToken() );
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("http://localhost:8081");
        cookie.setMaxAge(180);

        response.addCookie(cookie);
        response.sendRedirect("/me");
    }
}
