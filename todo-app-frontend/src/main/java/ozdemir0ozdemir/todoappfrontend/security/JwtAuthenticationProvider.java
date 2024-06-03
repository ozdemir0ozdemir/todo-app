package ozdemir0ozdemir.todoappfrontend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.dto.JwtResponse;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final TodoAppBackendClient backendClient;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // FIXME: Handle HttpClientErrorException for preventing interrupts caused backendClient
        JwtResponse jwtResponse;
        try {
            jwtResponse = backendClient.loginAndGetJwtToken(
                    (String) authentication.getPrincipal(),
                    (String) authentication.getCredentials());
        }
        catch (HttpClientErrorException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

        var auth = UsernamePasswordAuthenticationToken.authenticated(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                jwtResponse.getAuthorities()
        );

        // FIXME: Write custom details class to save timestamp, ip and jwt
        auth.setDetails(jwtResponse);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }


}
