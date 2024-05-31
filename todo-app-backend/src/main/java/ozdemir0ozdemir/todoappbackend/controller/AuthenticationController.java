package ozdemir0ozdemir.todoappbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0ozdemir.todoappbackend.dto.AuthenticationRequest;
import ozdemir0ozdemir.todoappbackend.dto.JwtResponse;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.service.JwtService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public final class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /*
     *  TODO: Refresh Token ?
     *  TODO: JWT 5m-15m
     *  TODO: Black List?
     *  TODO: Logout - Invalidate Token
     *
     *  LEARN : PBAC
     *  LEARN : JWT scope, rules
     *
     * */
    @PostMapping
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
                .build();

        return ResponseEntity.ok(response);
    }

}
