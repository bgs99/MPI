package ru.itmo.hungergames.service;

import ru.itmo.hungergames.model.entity.user.Sponsor;
import ru.itmo.hungergames.model.entity.user.User;
import ru.itmo.hungergames.model.response.JwtResponse;

public interface SecurityService {
    JwtResponse authenticateUser(User user);
    void createUser(User user);
    void createSponsor(Sponsor user);
    JwtResponse authenticateTributeAndMentor(User user);
}
