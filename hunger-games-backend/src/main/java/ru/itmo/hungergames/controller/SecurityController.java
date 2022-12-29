package ru.itmo.hungergames.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.request.SignInRequest;
import ru.itmo.hungergames.model.request.SignUpRequest;
import ru.itmo.hungergames.service.ModeratorService;
import ru.itmo.hungergames.service.SecurityService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class SecurityController {
    private final SecurityService securityService;
    private final ModeratorService moderatorService;

    @Autowired
    public SecurityController(SecurityService securityService,
                              ModeratorService moderatorService) {
        this.securityService = securityService;
        this.moderatorService = moderatorService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok((securityService.authenticateUser(new User(
                signInRequest.getUsername(),
                signInRequest.getPassword()))));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createSponsor(@Valid @RequestBody SignUpRequest signUpRequest) {
        securityService.createSponsor(new Sponsor(
                signUpRequest.getUsername(),
                signUpRequest.getPassword(),
                signUpRequest.getName(),
                signUpRequest.getAvatarUri()
        ));
        log.info("user {} is created", signUpRequest.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/moderator/signin")
    public ResponseEntity<?> generateTokenForModerator(@Valid @RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(moderatorService
                .authenticateModerator(new User(
                        signInRequest.getUsername(),
                        null
                )));
    }
}
