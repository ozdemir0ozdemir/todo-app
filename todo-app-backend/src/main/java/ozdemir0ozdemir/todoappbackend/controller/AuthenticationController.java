package ozdemir0ozdemir.todoappbackend.controller;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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

    @PostMapping
    public ResponseEntity<JwtResponse> getJwtToken(@RequestBody AuthenticationRequest authenticationRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        String path = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri()
                .getPath();

        Member member = (Member) auth.getPrincipal();

        JwtResponse response = JwtResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK)
                .token(jwtService.generateToken(member.getUsername()))
                .path(path)
                .build();

        return ResponseEntity.ok(response);
    }

}
