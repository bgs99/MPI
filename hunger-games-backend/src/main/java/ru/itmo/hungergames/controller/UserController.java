package ru.itmo.hungergames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.hungergames.model.request.SettingsChangeRequest;
import ru.itmo.hungergames.model.response.SettingsResponse;
import ru.itmo.hungergames.service.UserService;

@CrossOrigin("*")
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/settings")
    public void changeSettings(@RequestBody SettingsChangeRequest settingsChangeRequest) {
        userService.changeSettings(settingsChangeRequest);
    }

    @GetMapping("/settings")
    public SettingsResponse getSettings() {
        return userService.getSettings();
    }
}
