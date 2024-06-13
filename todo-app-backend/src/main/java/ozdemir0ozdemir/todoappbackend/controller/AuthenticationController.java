package ozdemir0ozdemir.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.todoappbackend.dto.AuthenticationRequest;
import ozdemir0ozdemir.todoappbackend.dto.JwtResponse;
import ozdemir0ozdemir.todoappbackend.dto.MeResponse;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.service.JwtService;
import ozdemir0ozdemir.todoappbackend.service.MemberService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public final class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MemberService memberService;

    /*
     *  TODO: Refreshing token by using special endpoint
     *  TODO: Access token expires about 5-15 minutes
     *  TODO: Refresh token expires in 15 days
     *  TODO: Logout triggers revoke token
     *  TODO: Revoke token by expiration
     *  TODO: Log to token creation and usage for fraud detection
     *
     *  LEARN : PBAC
     *  LEARN : JWT scope, rules
     *
     * */
    @PostMapping("/authentication")
    public ResponseEntity<JwtResponse> getJwtToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest servletRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        Member member = (Member) auth.getPrincipal();

        JwtResponse response = JwtResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .token(jwtService.generateToken(member.getUsername()))
                .path(servletRequest.getRequestURI())
                .authorities(member.getAuthorities())
                .build();

        return ResponseEntity.ok(response);
    }

    // TODO: even bearer token responses forbidden when authenticated() in security config
    @GetMapping("/me")
    public ResponseEntity<MeResponse> getMe(HttpServletRequest servletRequest) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return ResponseEntity.badRequest().build();
        }
        String jwt = servletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(jwt);
        Member member = memberService.loadUserByUsername(username);


        return ResponseEntity.ok(
                MeResponse.builder()
                        .username(username)
                        .token(jwt)
                        .authorities(member.getAuthorities())
                        .build()
        );
    }

}
