package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.request.CapitolSignInRequest;
import ru.itmo.hungergames.model.response.MentorResponse;
import ru.itmo.hungergames.model.response.TributeResponse;
import ru.itmo.hungergames.service.MentorService;
import ru.itmo.hungergames.service.SecurityService;
import ru.itmo.hungergames.service.TributeService;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/capitol") // TODO: certs identification with pub keys decrypt
public class CapitolController {
    private final SecurityService securityService;
    private final TributeService tributeService;
    private final MentorService mentorService;

    @Autowired
    public CapitolController(SecurityService securityService, TributeService tributeService, MentorService mentorService){
        this.securityService = securityService;
        this.tributeService = tributeService;
        this.mentorService = mentorService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> generateToken(@Valid @RequestBody CapitolSignInRequest capitolSignInRequest) {
        return ResponseEntity.ok(securityService
                .authenticateTributeAndMentor(new User(
                capitolSignInRequest.getUsername(),
                        null
                )));
    }


    @GetMapping("/mentor/all")
    public List<MentorResponse> getAllMentors() {
        return mentorService.getAllMentors();
    }


    @GetMapping("/tribute/all")
    public List<TributeResponse> getAllTributes() {
        return tributeService.getAllTributes();
    }
}
