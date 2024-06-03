package ozdemir0ozdemir.todoappfrontend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ozdemir0ozdemir.todoappfrontend.client.TodoAppBackendClient;
import ozdemir0ozdemir.todoappfrontend.filter.CookieJwtAuthFilter;
import ozdemir0ozdemir.todoappfrontend.security.JwtAuthenticationFailureHandler;
import ozdemir0ozdemir.todoappfrontend.security.JwtAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TodoAppBackendClient todoAppBackendClient;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(Customizer.withDefaults());

        http.formLogin(login -> {
            login.loginPage("/login")
                    .permitAll()
                    .successHandler(new JwtAuthenticationSuccessHandler())
                    .failureHandler(new JwtAuthenticationFailureHandler());
        });

        http.logout(LogoutConfigurer::permitAll);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request -> {
            request.requestMatchers("/login").permitAll();
            request.requestMatchers("/resources/**").permitAll();
            request.anyRequest().authenticated();
        });

        http.addFilterBefore(new CookieJwtAuthFilter(todoAppBackendClient), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

