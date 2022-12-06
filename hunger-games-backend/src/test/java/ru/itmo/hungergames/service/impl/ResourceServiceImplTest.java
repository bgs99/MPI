package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.repository.ResourceRepository;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class ResourceServiceImplTest {
    @Autowired
    ResourceServiceImpl resourceService;
    @MockBean
    ResourceRepository resourceRepository;
    @Test
    void getAllResources() {
        resourceService.getAllResources();
        Mockito.verify(resourceRepository, Mockito.times(1)).findAll();
    }
}