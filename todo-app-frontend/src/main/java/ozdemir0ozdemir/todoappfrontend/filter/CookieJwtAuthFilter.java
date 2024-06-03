package ozdemir0ozdemir.todoappfrontend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.dto.MeResponse;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class CookieJwtAuthFilter extends OncePerRequestFilter {

    private final TodoAppBackendClient backendClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("TodoAppToken"))
                .findFirst()
                .ifPresent(cookie -> {
                    // TODO: Error handling
                    MeResponse meResponse = backendClient.verifyMe(cookie.getValue());

                    var auth = UsernamePasswordAuthenticationToken.authenticated(
                            meResponse.getUsername(),
                            null,
                            meResponse.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);

                });

        filterChain.doFilter(request, response);
    }
}
