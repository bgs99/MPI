package ru.itmo.hungerGames.service;

import ru.itmo.hungerGames.model.Resource;

import javax.annotation.Resources;
import java.util.List;

public interface ResourceService {
    public List<Resource> getAllResources();
}
