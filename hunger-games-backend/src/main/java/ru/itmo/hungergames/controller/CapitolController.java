package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.request.CapitolSignInRequest;
import ru.itmo.hungergames.service.SecurityService;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/capitol") // TODO: certs identification with pub keys decrypt
public class CapitolController {
    private final SecurityService securityService;

    @Autowired
    public CapitolController(SecurityService securityService){
        this.securityService = securityService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@Valid @RequestBody CapitolSignInRequest capitolSignInRequest) {
        return ResponseEntity.ok(securityService
                .authenticateTributeAndMentor(new User(
                capitolSignInRequest.getUsername(),
                        null
                )));
    }
}
