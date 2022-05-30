package ru.itmo.hungerGames.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.hungerGames.model.Resource;
import ru.itmo.hungerGames.repository.ResourceRepository;
import ru.itmo.hungerGames.service.ResourceService;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }
}
