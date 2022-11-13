package ru.itmo.hungergames.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.request.SignInRequest;
import ru.itmo.hungergames.model.request.SignUpRequest;
import ru.itmo.hungergames.service.SecurityService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class SecurityController {
    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok((securityService.authenticateUser(new User(
                signInRequest.getUsername(),
                signInRequest.getPassword()))));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        securityService.createUser(new User(
                signUpRequest.getUsername(),
                signUpRequest.getPassword()
        ));
        log.info("user {} is created", signUpRequest.getUsername());
        return ResponseEntity.ok().build();
    }
}
