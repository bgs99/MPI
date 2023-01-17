package ru.itmo.hungergames.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.itmo.hungergames.model.entity.order.Resource;
import ru.itmo.hungergames.repository.ResourceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class ResourceServiceImplTest {
    @Autowired
    ResourceServiceImpl resourceService;
    @MockBean
    ResourceRepository resourceRepository;

    @Test
    void getAllResources() {
        Resource resource1 = Resource.builder()
                .id(new UUID(42, 42))
                .price(BigDecimal.valueOf(1))
                .name("resource1")
                .build();
        Resource resource2 = Resource.builder()
                .id(new UUID(42, 43))
                .price(BigDecimal.valueOf(1))
                .name("resource2")
                .build();

        var expectedResources = List.of(resource1, resource2);

        Mockito.doReturn(expectedResources).when(resourceRepository).findAll();

        assertEquals(expectedResources, resourceService.getAllResources());
    }
}