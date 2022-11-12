package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.User;
import ru.itmo.hungergames.model.response.JwtResponse;

public interface SecurityService {
    JwtResponse authenticateUser(User user);
    void createUser(User user);
}
