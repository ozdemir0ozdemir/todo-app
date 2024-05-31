package ozdemir0ozdemir.todoappbackend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ozdemir0ozdemir.todoappbackend.dto.ErrorResponse;
import ozdemir0ozdemir.todoappbackend.exception.GlobalExceptionHandler;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.service.JwtService;
import ozdemir0ozdemir.todoappbackend.service.MemberService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final JwtService jwtService;
    private final GlobalExceptionHandler exceptionHandler;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURL().toString().contains("/authentication")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);
        String username;

        try {
            username = jwtService.extractUsername(jwt);
        }
        catch (JwtException ex) {
            ResponseEntity<ErrorResponse> errorResponse = exceptionHandler.handleJwtException(ex, request);
            response.setStatus(errorResponse.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(errorResponse.getBody()));

            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Member member = (Member) this.memberService.loadUserByUsername(username);
            var authToken = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
