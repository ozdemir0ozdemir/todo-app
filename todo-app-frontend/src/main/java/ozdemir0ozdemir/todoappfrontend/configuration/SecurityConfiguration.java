package ozdemir0ozdemir.todoappfrontend.configuration;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.filter.CookieAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final ApplicationProperties applicationProperties;
    private final TodoAppBackendClient backendClient;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(Customizer.withDefaults());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/login").permitAll();
            request.requestMatchers("/error").permitAll();
            request.requestMatchers("/favicon.ico").permitAll();
            request.requestMatchers("/resources/**").permitAll();
            request.requestMatchers("/admin").hasAuthority("ADMIN");
            request.anyRequest().authenticated();
        });

        http.exceptionHandling(handling -> {

            handling.accessDeniedHandler((request, response, accessDeniedException) -> {

                response.setHeader("Referer", "AccessDeniedHandler");
                response.sendRedirect("accessdenied");
            });

            handling.authenticationEntryPoint((request, response, authException) -> {

                boolean cookieAuth = false;
                if(request.getCookies() != null) {
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equals("TodoAppCookie")) {
                            cookieAuth = true;
                        }
                    }
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if(authentication == null && !cookieAuth){
                    response.sendRedirect("/login");
                }
            });

        });

        http.addFilterBefore(new CookieAuthenticationFilter(backendClient), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) {
        builder.eraseCredentials(false);
        return builder.getOrBuild();
    }

}

