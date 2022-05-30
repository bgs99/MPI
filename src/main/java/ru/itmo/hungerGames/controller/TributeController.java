package ru.itmo.hungerGames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.hungerGames.model.Tribute;
import ru.itmo.hungerGames.service.TributeService;

import java.util.List;

@RestController
@RequestMapping("/api/tribute")
public class TributeController {
    private final TributeService tributeService;

    @Autowired
    public TributeController(TributeService tributeService) {
        this.tributeService = tributeService;
    }

    @GetMapping("/all")
    public List<Tribute> getAllTributes() {
        return tributeService.getAllTributes();
    }
}
