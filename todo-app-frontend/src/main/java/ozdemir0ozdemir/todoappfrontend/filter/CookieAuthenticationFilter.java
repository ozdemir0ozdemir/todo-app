package ozdemir0ozdemir.todoappfrontend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.dto.MeResponse;
import ozdemir0ozdemir.todoappfrontend.exception.LoginFailedException;

import java.io.IOException;

@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {

    private final TodoAppBackendClient backendClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getCookies() == null) {
            filterChain.doFilter(request, response);
            return;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("TodoAppCookie")) {

                MeResponse meResponse;
                try{
                    meResponse = backendClient.verifyMe(cookie.getValue());
                }
                catch (LoginFailedException ex) {
                    Cookie removedCookie = new Cookie("TodoAppCookie", "");
                    removedCookie.setMaxAge(0);
                    removedCookie.setHttpOnly(true);
                    removedCookie.setSecure(true);
                    response.addCookie(removedCookie);
                    response.sendRedirect("login");
                    return;
                }


                var token = new UsernamePasswordAuthenticationToken(
                        meResponse.getUsername(),
                        cookie.getValue(),
                        meResponse.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }

        filterChain.doFilter(request, response);
    }
}
